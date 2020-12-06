package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.models.Location
import nl.woonwaard.wij_maken_de_wijk.domain.models.Registration
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager

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

        val (locationCode, hallwayCode, houseNumberCode) = code.split('-').map { code ->
            code.map { getOriginalCharacter(it).toString() }
        }

        val actualLocationId = (locationCode.joinToString(separator = "")).toInt(16)
        val actualHallwayCode = hallwayCode.joinToString(separator = "")
        val actualHouseNumberCode = houseNumberCode.joinToString(separator = "")

        // TODO: validation to make sure is not null
        val location = Location.values().find { it.locationId == actualLocationId }?.fullName ?: return

        viewModelScope.launch {
            val createdAccount = accountManager.createAccount(
                Registration(email, password, name, actualHouseNumberCode, actualHallwayCode, location)
            )
            val result =
                if(createdAccount != null) accountManager.login(Credentials(email, password))
                else false
            mutableIsLoading.postValue(false)
            mutableIsLoggedIn.postValue(result)
        }
    }

    companion object {
        private val characters = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        )

        private const val shiftIndex = 24

        private val shiftedCharacters = characters.copyOfRange(0, shiftIndex).reversedArray() +
                characters.copyOfRange(shiftIndex + 1, characters.size)

        fun getOriginalCharacter(character: Char): Char = characters[shiftedCharacters.indexOf(character)]
    }
}
