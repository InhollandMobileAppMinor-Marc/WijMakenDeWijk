package nl.woonwaard.wij_maken_de_wijk.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import nl.woonwaard.wij_maken_de_wijk.api.utils.edit
import nl.woonwaard.wij_maken_de_wijk.api.utils.getPreferences
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.*
import okhttp3.CertificatePinner
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class WmdwApi(context: Context) : PostsRepository, CommentsRepository, UsersRepository, NotificationsRepository, AccountManager, ApiStatusController {
    override var token: String? = null

    private val authHeaderValue: String?
        get() = if(token == null) null else "Bearer $token"

    private val preferences = context.getPreferences("nl.woonwaard.wij_maken_de_wijk.account", true)

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://wij-maken-de-wijk.herokuapp.com/api/v0/")
            .client(
                OkHttpClient.Builder()
                    .certificatePinner(
                        CertificatePinner.Builder()
                            .add(
                                "wij-maken-de-wijk.herokuapp.com",
                                "sha256/Vuy2zjFSPqF5Hz18k88DpUViKGbABaF3vZx5Raghplc=",
                                "sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=",
                                "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18="
                            )
                            .build()
                    )
                    .connectTimeout(45, TimeUnit.SECONDS)
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
            getPosts(authHeaderValue ?: "N/A", categories?.joinToString(","))
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun getPostById(id: String): Post? {
        val response = api {
            getPostById(authHeaderValue ?: "N/A", id)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun addPost(createdPost: CreatedPost): Post? {
        val response = api {
            addPost(authHeaderValue ?: "N/A", createdPost)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun getAllUsers(): Set<User> {
        val response = api {
            getUsers(authHeaderValue ?: "N/A")
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun getUserById(id: String): User? {
        val response = api {
            getUserById(authHeaderValue ?: "N/A", id)
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

        preferences.edit {
            putString(PREFERENCES_TOKEN, token)
        }

        return token != null
    }

    override fun logout() {
        preferences.edit {
            remove(PREFERENCES_TOKEN)
        }
        token = null
    }

    override suspend fun getApiStatus(): ApiStatus {
        val response = api {
            getStatus(authHeaderValue)
        }

        val status = if (response?.isSuccessful == true) response.body() else null

        return when {
            status == null -> ApiStatus.ConnectionError
            status.loggedIn && status.user != null -> ApiStatus.LoggedIn(status.user)
            else -> ApiStatus.LoggedOut
        }
    }

    override suspend fun getCommentsForPost(post: Post): Set<Comment> {
        val response = api {
            getCommentsForPost(authHeaderValue ?: "N/A", post.id)
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    override suspend fun addCommentToPost(post: Post, comment: CreatedComment): Comment? {
        val response = api {
            addCommentToPost(authHeaderValue ?: "N/A", post.id, comment)
        }

        return if (response?.isSuccessful == true) response.body() else null
    }

    override suspend fun getNewNotifications(): Set<Notification> {
        val response = api {
            getNewNotifications(authHeaderValue ?: "N/A")
        }

        return if (response?.isSuccessful == true) response.body() ?: emptySet() else emptySet()
    }

    companion object {
        const val PREFERENCES_TOKEN = "TOKEN"
    }
}
