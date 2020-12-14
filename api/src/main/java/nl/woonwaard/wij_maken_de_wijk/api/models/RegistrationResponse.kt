package nl.woonwaard.wij_maken_de_wijk.api.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

@Serializable
data class RegistrationResponse(
    val id: String,
    val email: String,
    val name: String,
    val role: String,
    val houseNumber: String,
    val hallway: String,
    val location: String
) {
    val user: User
        get() = User(id, name, role, houseNumber, hallway, location)
}
