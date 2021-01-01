package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.SettingsNavigationService

class SettingsNavigationServiceImplementation(
    val context: Context
) : SettingsNavigationService {
    override fun getOverviewIntent(): Intent {
        return Intent(context, SettingsActivity::class.java)
    }
}
