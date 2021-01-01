package nl.woonwaard.wij_maken_de_wijk.ui.core

import android.content.ComponentName
import android.content.Context
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CustomTabsHelper {
    fun createSession(
        context: Context,
        customTabsCallback: CustomTabsCallback = CustomTabsCallback()
    ): LiveData<CustomTabsSession?> {
        val customTabsSession = MutableLiveData<CustomTabsSession?>(null)

        val customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                client.warmup(0)
                customTabsSession.postValue(client.newSession(customTabsCallback))
            }

            override fun onServiceDisconnected(name: ComponentName) {
                customTabsSession.postValue(null)
            }
        }

        CustomTabsClient.bindCustomTabsService(context, context.packageName, customTabsServiceConnection)

        return customTabsSession
    }
}
