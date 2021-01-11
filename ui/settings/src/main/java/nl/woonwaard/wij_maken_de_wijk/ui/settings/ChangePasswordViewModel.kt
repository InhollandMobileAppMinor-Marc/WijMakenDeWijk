package nl.woonwaard.wij_maken_de_wijk.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.AccountManager

class ChangePasswordViewModel(
    private val accountManager: AccountManager
) : ViewModel() {
    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    val changePasswordResult = MutableLiveData<Boolean>(null)

    private var job: Job? = null

    fun changePassword(email: String, oldPassword: String, newPassword: String) {
        mutableIsLoading.postValue(true)

        job = viewModelScope.launch {
            val hasChanged = accountManager.changePassword(Credentials(email, oldPassword), newPassword)
            mutableIsLoading.postValue(false)
            if(isActive) changePasswordResult.postValue(hasChanged)
        }
    }

    fun isPasswordCorrect(password: String) =
        password.length in 4 until 64 &&
                password.any { it.isDigit() } &&
                password.any { it.isLetter() && it.isLowerCase() } &&
                password.any { it.isLetter() && it.isUpperCase() }

    fun clearRunningJobs() {
        changePasswordResult.postValue(null)

        viewModelScope.launch {
            job?.cancelAndJoin()
            job = null
            mutableIsLoading.postValue(false)
        }
    }
}
