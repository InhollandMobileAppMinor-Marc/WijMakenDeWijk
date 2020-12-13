package nl.woonwaard.wij_maken_de_wijk.api.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

@Serializable
data class StatusResponse(
    val loggedIn: Boolean,
    val user: User?
)
