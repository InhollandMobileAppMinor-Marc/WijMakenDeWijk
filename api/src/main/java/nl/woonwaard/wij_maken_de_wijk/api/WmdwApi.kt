package nl.woonwaard.wij_maken_de_wijk.api

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.api.utils.edit
import nl.woonwaard.wij_maken_de_wijk.api.utils.getPreferences
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.CrashReporter
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

class WmdwApi(
    context: Context,
    private val crashReporter: CrashReporter
) : PostsRepository, CommentsRepository, UsersRepository, NotificationsRepository, AccountManager, ApiStatusController {
    override var token: String? = null

    override var user: User? = null
        set(value) {
            field = value
            if(value != null) crashReporter.setUserId(value.id)
            else crashReporter.clearUserId()
        }

    override val isLoggedIn: Boolean
        get() = token != null

    private val authHeaderValue: String?
        get() = if(token == null) null else "Bearer $token"

    private val preferences = context.getPreferences("nl.woonwaard.wij_maken_de_wijk.account", true)

    private val api by lazy {
        ApiClient.retrofitApi
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
                crashReporter.logSoftError(error)
                null
            } catch (error: SocketTimeoutException) {
                crashReporter.logSoftError(error)
                null
            } catch (error: SSLPeerUnverifiedException) {
                crashReporter.logSoftError(error)
                null
            }
        }
    }

    override suspend fun getAllPosts(categories: Set<String>?): Set<Post> {
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

    override suspend fun deletePost(post: Post) = deletePost(post.id)

    override suspend fun deletePost(id: String): Boolean {
        val response = api {
            deletePostById(authHeaderValue ?: "N/A", id)
        }

        return response?.isSuccessful == true
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

    override suspend fun deleteUser(user: User) = deleteUser(user.id)

    override suspend fun deleteUser(id: String): Boolean {
        val response = api {
            deleteUserById(authHeaderValue ?: "N/A", id)
        }

        return response?.isSuccessful == true
    }

    override suspend fun createAccount(registration: Registration): User? {
        val response = api {
            register(registration)
        }

        val user = (if (response?.isSuccessful == true) response.body()?.user else null) ?: return null

        val success = login(Credentials(registration.email, registration.password))

        if(!success) return null

        this.user = user
        return user
    }

    override suspend fun login(credentials: Credentials): Boolean {
        val response = api {
            login(credentials)
        }

        token = if (response?.isSuccessful == true) response.body()?.token else null

        preferences.edit {
            putString(PREFERENCES_TOKEN, token)
        }

        return isLoggedIn
    }

    override fun logout() {
        preferences.edit {
            remove(PREFERENCES_TOKEN)
        }
        token = null
        user = null
    }

    override suspend fun deleteAccount(): Boolean {
        val loggedInUser = user ?: getApiStatus().let {
            if (it is ApiStatus.LoggedIn) it.user else null
        } ?: return false

        val success = deleteUser(loggedInUser)
        if(success) logout()
        return success
    }

    override suspend fun getApiStatus(): ApiStatus {
        val response = api {
            getStatus(authHeaderValue)
        }

        val status = if (response?.isSuccessful == true) response.body() else null

        return when {
            status == null -> ApiStatus.ConnectionError
            status.loggedIn && status.user != null -> {
                user = status.user
                ApiStatus.LoggedIn(status.user)
            }
            else -> {
                logout()
                ApiStatus.LoggedOut
            }
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

    override suspend fun deleteComment(comment: Comment) = deleteComment(comment.id)

    override suspend fun deleteComment(id: String): Boolean {
        val response = api {
            deleteCommentById(authHeaderValue ?: "N/A", id)
        }

        return response?.isSuccessful == true
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
