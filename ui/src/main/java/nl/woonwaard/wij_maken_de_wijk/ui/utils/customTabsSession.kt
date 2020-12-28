package nl.woonwaard.wij_maken_de_wijk.ui.utils

import android.content.Context

fun Context.customTabsSession() = lazy { CustomTabsHelper.createSession(this) }
