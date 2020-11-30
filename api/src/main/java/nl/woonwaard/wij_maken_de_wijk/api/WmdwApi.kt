package nl.woonwaard.wij_maken_de_wijk.api

import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.UsersRepository
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WmdwApi : PostsRepository, CommentsRepository, UsersRepository, AccountManager {
    override var token: String? = null

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

    override suspend fun getAllPosts(): Set<Post> {
        val response = api {
            getPosts("Bearer $token")
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

        return token != null
    }

    override suspend fun logout() {
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
}
