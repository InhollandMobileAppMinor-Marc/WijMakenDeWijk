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
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.ApiStatusController

class ChangeEmailViewModel(
    private val accountManager: AccountManager
) : ViewModel() {
    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    val changeEmailResult = MutableLiveData<Boolean>(null)

    private var job: Job? = null

    fun changeEmail(newEmail: String, oldEmail: String, password: String) {
        mutableIsLoading.postValue(true)

        job = viewModelScope.launch {
            val hasChanged = accountManager.changeEmail(Credentials(oldEmail, password), newEmail)
            mutableIsLoading.postValue(false)
            if(isActive) changeEmailResult.postValue(hasChanged)
        }
    }

    fun isEmailCorrect(email: String) = emailRegExp.toRegex().matches(email)

    fun clear() {
        changeEmailResult.postValue(null)

        viewModelScope.launch {
            job?.cancelAndJoin()
            job = null
            mutableIsLoading.postValue(false)
        }
    }

    companion object {
        const val emailRegExp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|" +
                "\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[" +
                "\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[" +
                "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    }
}
