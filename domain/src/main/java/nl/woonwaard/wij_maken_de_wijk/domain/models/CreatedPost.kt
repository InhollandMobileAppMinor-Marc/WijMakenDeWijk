package nl.woonwaard.wij_maken_de_wijk.domain.models

import kotlinx.serialization.Serializable
import nl.woonwaard.wij_maken_de_wijk.domain.utils.DateSerializer
import java.util.*

@Serializable
data class CreatedPost(
    val title: String,
    val category: String,
    val body: String,
    @Serializable(with = DateSerializer::class)
    val timestamp: Date
) : java.io.Serializable
