package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.AuthenticationNavigationService

class AuthenticationNavigationServiceImplementation(
    val context: Context
) : AuthenticationNavigationService {

    override fun getLoginIntent(): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun getRegistrationIntent(): Intent {
        return Intent(context, RegistrationActivity::class.java)
    }

}
