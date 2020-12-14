package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val post: Post,
    val comments: Set<Comment>
) : java.io.Serializable
