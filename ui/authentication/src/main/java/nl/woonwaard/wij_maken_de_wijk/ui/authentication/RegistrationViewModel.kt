package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Registration
import nl.woonwaard.wij_maken_de_wijk.domain.models.RegistrationCodes
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.AccountManager

class RegistrationViewModel(
    private val accountManager: AccountManager
) : ViewModel() {
    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    private val mutableIsLoggedIn = MutableLiveData(false)

    val isLoggedIn: LiveData<Boolean>
        get() = mutableIsLoggedIn

    fun signup(code: String, name: String, email: String, password: String) {
        mutableIsLoading.postValue(true)

        val (location, hallway, houseNumber) = RegistrationCodes.getCodeData(code)

        if(location == null) {
            mutableIsLoading.postValue(false)
            return
        }

        viewModelScope.launch {
            accountManager.createAccount(
                Registration(email, password, name, houseNumber, hallway, location)
            )
            mutableIsLoading.postValue(false)
            mutableIsLoggedIn.postValue(accountManager.isLoggedIn)
        }
    }

    fun isCodeCorrect(code: String) =
        RegistrationCodes.codeRegExp.toRegex().matches(code) &&
                RegistrationCodes.getCodeData(code).first != null

    fun isNameCorrect(name: String) = name.length in 2 until 64 && name.none { it.isDigit() }

    fun isEmailCorrect(email: String) = emailRegExp.toRegex().matches(email)

    fun isPasswordCorrect(password: String) =
        password.length in 4 until 64 &&
                password.any { it.isDigit() } &&
                password.any { it.isLetter() && it.isLowerCase() } &&
                password.any { it.isLetter() && it.isUpperCase() }

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
