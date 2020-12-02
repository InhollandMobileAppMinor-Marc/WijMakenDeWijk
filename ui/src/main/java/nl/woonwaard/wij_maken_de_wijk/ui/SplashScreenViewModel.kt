package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager

class SplashScreenViewModel(
    val accountManager: AccountManager
) : ViewModel() {
    private val mutableShouldShowSplashScreen = MutableLiveData(SplashScreenState.SHOULD_SHOW)

    val shouldShowSplashScreen: LiveData<SplashScreenState>
        get() = mutableShouldShowSplashScreen

    val isLoggedIn: Boolean
        get() = accountManager.token != null

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

    enum class SplashScreenState {
        SHOULD_SHOW, SHOWN, SHOULD_DISMISS, DISMISSED
    }

    companion object {
        const val DELAY = 2000L
    }
}
