package nl.woonwaard.wij_maken_de_wijk.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class WmdwApi(context: Context) : PostsRepository, CommentsRepository, UsersRepository, NotificationsRepository, AccountManager {
    override var token: String? = null

    private val preferences = context.getSharedPreferences("account", Context.MODE_PRIVATE)

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://wij-maken-de-wijk.herokuapp.com/api/v0/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(WmdwApiSpecification::class.java)
    }

    init {
        token = preferences.getString(PREFERENCES_TOKEN, null)
    }

    private suspend inline fun <T> api(crossinline block: suspend WmdwApiSpecification.() -> T): T? {
        return withContext(Dispatchers.IO) {
            try {
                val response = block(api)
                response
            } catch (error: UnknownHostException) {
                // Network is offline
                null
            } catch (error: SocketTimeoutException) {
                null
            }
        }
    }

    override suspend fun getAllPosts(categories: List<String>?): Set<Post> {
        val response = api {
            getPosts("Bearer $token", categories?.joinToString(","))
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun getPostById(id: String): Post? {
        val response = api {
            getPostById("Bearer $token", id)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun addPost(createdPost: CreatedPost): Post? {
        val response = api {
            addPost("Bearer $token", createdPost)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun getAllUsers(): Set<User> {
        val response = api {
            getUsers("Bearer $token")
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun getUserById(id: String): User? {
        val response = api {
            getUserById("Bearer $token", id)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun createAccount(registration: Registration): User? {
        val response = api {
            register(registration)
        }

        return if (response?.isSuccessful == true) response.body()?.user else null
    }

    override suspend fun login(credentials: Credentials): Boolean {
        val response = api {
            login(credentials)
        }

        token = if (response?.isSuccessful == true) response.body()?.token else null

        preferences.edit().putString(PREFERENCES_TOKEN, token).apply()

        return token != null
    }

    override fun logout() {
        preferences.edit().remove(PREFERENCES_TOKEN).apply()
        token = null
    }

    override suspend fun getCommentsForPost(post: Post): Set<Comment> {
        val response = api {
            getCommentsForPost("Bearer $token", post.id)
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun addCommentToPost(post: Post, comment: CreatedComment): Comment? {
        val response = api {
            addCommentToPost("Bearer $token", post.id, comment)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun getNewNotifications(): Set<Notification> {
        val response = api {
            getNewNotifications("Bearer $token")
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    companion object {
        const val PREFERENCES_TOKEN = "TOKEN"
    }
}
