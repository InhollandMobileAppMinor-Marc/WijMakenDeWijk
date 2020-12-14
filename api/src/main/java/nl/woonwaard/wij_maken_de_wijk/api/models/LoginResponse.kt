package nl.woonwaard.wij_maken_de_wijk.api.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
