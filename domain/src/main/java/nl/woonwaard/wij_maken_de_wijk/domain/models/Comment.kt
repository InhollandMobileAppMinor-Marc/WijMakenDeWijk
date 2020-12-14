package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.utils.DateSerializer
import java.util.*

@Serializable
data class Comment(
    val id: String,
    val body: String,
    val author: User,
    @Serializable(with = DateSerializer::class)
    val timestamp: Date,
    val post: String
) : java.io.Serializable
