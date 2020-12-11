package nl.woonwaard.wij_maken_de_wijk.notifications.utils

import android.content.Intent
import java.io.Serializable

fun Intent.putExtra(name: String, set: Set<Serializable>): Intent {
    var intent: Intent = this.putExtra(name, set.size)
    for(i in set.indices) {
        intent = intent.putExtra("${name}_$i", set.elementAt(i))
    }
    return intent
}
