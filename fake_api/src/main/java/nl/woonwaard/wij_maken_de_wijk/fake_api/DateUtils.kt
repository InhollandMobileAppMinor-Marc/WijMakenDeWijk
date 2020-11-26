package nl.woonwaard.wij_maken_de_wijk.fake_api

import java.util.*

fun createDate(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int
) = Date(year - 1900, month - 1, day, hour, minute)
