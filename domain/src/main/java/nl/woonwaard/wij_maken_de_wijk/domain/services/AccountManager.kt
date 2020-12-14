package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.models.Registration
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

interface AccountManager {
    val token: String?

    suspend fun createAccount(registration: Registration): User?

    suspend fun login(credentials: Credentials): Boolean

    fun logout()
}
