package nl.woonwaard.wij_maken_de_wijk.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationWorkScheduler

class SettingsViewModel(
    private val accountManager: AccountManager,
    private val notificationWorkScheduler: NotificationWorkScheduler
) : ViewModel() {
    private val mutableIsLoggedIn = MutableLiveData(true)

    val isLoggedIn: LiveData<Boolean>
        get() = mutableIsLoggedIn

    fun logout() {
        accountManager.logout()
        mutableIsLoggedIn.postValue(false)
    }

    fun ensureCorrectNotificationConfiguration(areNotificationsEnabled: Boolean = true) {
        if(areNotificationsEnabled) notificationWorkScheduler.schedule()
        else notificationWorkScheduler.cancel()
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val accountDeleted = accountManager.deleteAccount()
            mutableIsLoggedIn.postValue(!accountDeleted)
        }
    }
}
