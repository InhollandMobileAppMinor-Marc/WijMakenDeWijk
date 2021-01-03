package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

import android.content.Intent

interface MainNavigationService {
    fun getHomeScreenIntent(): Intent

    fun getSplashScreenIntent(): Intent
}
