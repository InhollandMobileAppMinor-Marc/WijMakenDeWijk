package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.ApiStatus
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.ApiStatusController
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationWorkScheduler

class SplashScreenViewModel(
    private val accountManager: AccountManager,
    private val notificationWorkScheduler: NotificationWorkScheduler,
    private val apiStatusController: ApiStatusController
) : ViewModel() {
    private val mutableShouldShowSplashScreen = MutableLiveData(SplashScreenState.SHOULD_SHOW)

    val shouldShowSplashScreen: LiveData<SplashScreenState>
        get() = mutableShouldShowSplashScreen

    private val mutableApiStatus = MutableLiveData<ApiStatus>()

    val apiStatus: LiveData<ApiStatus>
        get() = mutableApiStatus

    val isLoggedIn: Boolean
        get() = accountManager.token != null

    fun ensureCorrectNotificationConfiguration(areNotificationsEnabled: Boolean = true) {
        if(areNotificationsEnabled) notificationWorkScheduler.schedule()
        else notificationWorkScheduler.cancel()
    }

    fun onSplashScreenShown() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.SHOWN)

        viewModelScope.launch {
            val status = apiStatusController.getApiStatus()
            mutableApiStatus.postValue(status)
            mutableShouldShowSplashScreen.postValue(SplashScreenState.SHOULD_DISMISS)
        }
    }

    fun onDismissed() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.DISMISSED)
    }

    fun onCancelled() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.CANCELLED)
    }

    enum class SplashScreenState {
        SHOULD_SHOW, SHOWN, SHOULD_DISMISS, DISMISSED, CANCELLED
    }
}
