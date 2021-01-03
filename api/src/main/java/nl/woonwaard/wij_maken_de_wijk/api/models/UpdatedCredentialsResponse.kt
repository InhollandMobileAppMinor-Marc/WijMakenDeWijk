package nl.woonwaard.wij_maken_de_wijk.api.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

@Serializable
data class UpdatedCredentialsResponse(
    val user: String,
    val email: String
)
