package nl.woonwaard.wij_maken_de_wijk.notifications.utils

import android.content.BroadcastReceiver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

inline fun BroadcastReceiver.launch(crossinline block: suspend () -> Unit) {
    val pendingResult = goAsync()
    GlobalScope.launch {
        block()
        pendingResult.finish()
    }
}
