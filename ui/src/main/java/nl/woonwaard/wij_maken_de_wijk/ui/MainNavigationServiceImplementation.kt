package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.MainNavigationService

class MainNavigationServiceImplementation(
    val context: Context
) : MainNavigationService {
    override fun getHomeScreenIntent(): Intent {
        return Intent(context, MainActivity::class.java)
    }
}
