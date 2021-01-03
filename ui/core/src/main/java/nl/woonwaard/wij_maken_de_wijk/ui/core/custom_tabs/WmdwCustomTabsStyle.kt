package nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import nl.woonwaard.wij_maken_de_wijk.ui.core.R

object WmdwCustomTabsStyle {
    fun createCustomTabsSessionWithWmdwStyle(context: Context, customTabsSession: CustomTabsSession? = null): CustomTabsIntent {
        val customTabsBuilder = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(
                        CustomTabColorSchemeParams.Builder()
                                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                                .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorAccent))
                                .build()
                )
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                .setUrlBarHidingEnabled(true)
                .setShowTitle(true)

        if(customTabsSession != null) customTabsBuilder.setSession(customTabsSession)

        return customTabsBuilder.build()
    }
}
