package nl.woonwaard.wij_maken_de_wijk.domain.models

sealed class ApiStatus {
    object LoggedOut : ApiStatus()
    object ConnectionError : ApiStatus()
    class LoggedIn(val user: User) : ApiStatus()
}
