package nl.woonwaard.wij_maken_de_wijk.domain.models

import java.util.*

data class Comment(
    val id: Int,
    val body: String,
    val creatorAccountId: Int,
    val creationDate: Date
)
