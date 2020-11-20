package nl.woonwaard.wij_maken_de_wijk.domain.models

import java.util.*

data class Post(
    val id: Int,
    val title: String,
    val type: PostType,
    val body: String,
    val creatorAccountId: Int,
    val creationDate: Date,
    val commentIds: List<Int>
)
