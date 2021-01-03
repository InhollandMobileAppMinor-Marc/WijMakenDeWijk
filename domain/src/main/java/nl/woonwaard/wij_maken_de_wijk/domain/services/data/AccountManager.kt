package nl.woonwaard.wij_maken_de_wijk.domain.services.data

import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.models.Registration
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

interface AccountManager {
    val token: String?

    val user: User?

    val isLoggedIn: Boolean

    suspend fun createAccount(registration: Registration): User?

    suspend fun login(credentials: Credentials): Boolean

    suspend fun changeEmail(email: String): Boolean

    suspend fun changePassword(password: String): Boolean

    fun logout()

    suspend fun deleteAccount(): Boolean
}
