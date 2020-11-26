package nl.woonwaard.wij_maken_de_wijk.domain.models

import java.io.Serializable
import java.util.*

data class Post(
    val id: Int,
    val title: String,
    val type: PostType,
    val body: String,
    val creator: User,
    val creationDate: Date,
    val commentIds: MutableSet<Int>
) : Serializable
