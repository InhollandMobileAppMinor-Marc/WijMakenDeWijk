package nl.woonwaard.wij_maken_de_wijk.domain.utils

import android.content.Intent
import java.io.Serializable

fun Intent.putSerializableArrayExtra(name: String, value: Array<out Serializable?>) {
    putExtra(name, value.size)
    for (i in value.indices) putExtra("${name}_$i", value[i])
}

fun Intent.getSerializableArrayExtra(name: String): Array<Serializable?> {
    val size = getIntExtra(name, 0)
    return if(size > 0) Array(size) { getSerializableExtra("${name}_$it") } else emptyArray()
}
