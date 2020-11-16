package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    private val mutableShouldShowSplashScreen = MutableLiveData(SplashScreenState.SHOULD_SHOW)

    val shouldShowSplashScreen: LiveData<SplashScreenState>
        get() = mutableShouldShowSplashScreen

    fun onSplashScreenShown() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.SHOWN)
        viewModelScope.launch {
            delay(1000)
            mutableShouldShowSplashScreen.postValue(SplashScreenState.SHOULD_DISMISS)
        }
    }

    fun onDismissed() {
        mutableShouldShowSplashScreen.postValue(SplashScreenState.DISMISSED)
    }

    enum class SplashScreenState {
        SHOULD_SHOW, SHOWN, SHOULD_DISMISS, DISMISSED
    }
}
