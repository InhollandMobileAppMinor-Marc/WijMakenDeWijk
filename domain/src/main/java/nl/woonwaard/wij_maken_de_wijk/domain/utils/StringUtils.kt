package nl.woonwaard.wij_maken_de_wijk.domain.utils

import java.util.*

fun String.toSentenceCasing() = first().toUpperCase() + substring(1).toLowerCase(Locale.getDefault())
