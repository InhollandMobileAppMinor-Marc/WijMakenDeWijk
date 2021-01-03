package nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs

import android.content.Context

fun Context.customTabsSession() = lazy { CustomTabsHelper.createSession(this) }
