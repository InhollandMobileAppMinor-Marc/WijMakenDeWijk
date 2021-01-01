package nl.woonwaard.wij_maken_de_wijk.ui.core

import android.content.Context

fun Context.customTabsSession() = lazy { CustomTabsHelper.createSession(this) }
