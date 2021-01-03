package nl.woonwaard.wij_maken_de_wijk.ui.main

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.MainNavigationService

class MainNavigationServiceImplementation(
    private val context: Context
) : MainNavigationService {
    override fun getHomeScreenIntent(): Intent {
        return Intent(context, MainActivity::class.java)
    }

    override fun getSplashScreenIntent(): Intent {
        return Intent(context, SplashScreenActivity::class.java)
    }
}
