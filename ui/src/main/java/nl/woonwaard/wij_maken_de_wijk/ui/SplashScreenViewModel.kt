package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.NotificationWorkScheduler

class SplashScreenViewModel(
    private val accountManager: AccountManager,
    notificationWorkScheduler: NotificationWorkScheduler
) : ViewModel() {
    private val mutableShouldShowSplashScreen = MutableLiveData(SplashScreenState.SHOULD_SHOW)

    val shouldShowSplashScreen: LiveData<SplashScreenState>
        get() = mutableShouldShowSplashScreen

    val isLoggedIn: Boolean
        get() = accountManager.token != null

    init {
        notificationWorkScheduler.schedule()
    }

    fun onSplashScreenShown() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.SHOWN)
        viewModelScope.launch {
            delay(DELAY)
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

    companion object {
        const val DELAY = 2000L
    }
}
