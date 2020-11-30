package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val email: String,
    val password: String
): java.io.Serializable
