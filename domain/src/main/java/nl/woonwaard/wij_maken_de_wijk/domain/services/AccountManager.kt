package nl.woonwaard.wij_maken_de_wijk.domain.services

interface AccountManager {
    val credentials: Credentials?

    suspend fun createAccount(username: String, password: String)

    suspend fun login(username: String, password: String)

    suspend fun logout()

    interface Credentials
}
