package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val role: String,
    val houseNumber: String,
    val hallway: String,
    val location: String
) : java.io.Serializable
