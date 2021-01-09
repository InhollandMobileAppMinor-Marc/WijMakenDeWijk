package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val role: String,
    val houseNumber: String,
    val hallway: String,
    val location: String,
    val deleted: Boolean
) : java.io.Serializable {
    val nameWithLocation: String
        get() = if(houseNumber.isEmpty()) name else "$name ($houseNumber)"

    val isAdmin: Boolean
        get() = role == "admin"
}
