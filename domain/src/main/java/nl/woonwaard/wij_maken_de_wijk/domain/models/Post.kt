package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.utils.DateSerializer
import java.util.*

@Serializable
data class Post(
    val id: String,
    val title: String,
    val category: String,
    val body: String,
    val author: User,
    @Serializable(with = DateSerializer::class)
    val timestamp: Date,
    val comments: Set<String>,
    val hallway: String,
    val location: String
) : java.io.Serializable
