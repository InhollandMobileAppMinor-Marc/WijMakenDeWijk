package nl.woonwaard.wij_maken_de_wijk.ui.utils

import android.os.Process.killProcess
import android.os.Process.myPid
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

fun AppCompatActivity.terminateApplication() {
    moveTaskToBack(true)
    killProcess(myPid())
    exitProcess(1)
}
