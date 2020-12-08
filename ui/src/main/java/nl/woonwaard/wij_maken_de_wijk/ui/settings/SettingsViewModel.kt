package nl.woonwaard.wij_maken_de_wijk.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager

class SettingsViewModel(
    private val accountManager: AccountManager
) : ViewModel() {
    private val mutableIsLoggedIn = MutableLiveData(true)

    val isLoggedIn: LiveData<Boolean>
        get() = mutableIsLoggedIn

    fun logout() {
        accountManager.logout()
        mutableIsLoggedIn.postValue(false)
    }
}
