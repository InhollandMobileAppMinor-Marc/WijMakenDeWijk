package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager

class LoginViewModel(
    private val accountManager: AccountManager
) : ViewModel() {
    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    private val mutableIsLoggedIn = MutableLiveData(false)

    val isLoggedIn: LiveData<Boolean>
        get() = mutableIsLoggedIn

    fun login(email: String, password: String, error: (() -> Unit)? = null) {
        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val isLoggedIn = accountManager.login(Credentials(email, password))
            mutableIsLoading.postValue(false)
            mutableIsLoggedIn.postValue(isLoggedIn)
            if (!isLoggedIn) error?.invoke()
        }
    }
}
