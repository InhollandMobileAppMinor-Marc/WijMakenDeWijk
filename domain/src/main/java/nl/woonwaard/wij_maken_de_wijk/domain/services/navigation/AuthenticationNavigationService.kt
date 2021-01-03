package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

import android.content.Intent

interface AuthenticationNavigationService {
    fun getLoginIntent(): Intent

    fun getRegistrationIntent(): Intent

    fun getInviteScreenIntent(): Intent
}
