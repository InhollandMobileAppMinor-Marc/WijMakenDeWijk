package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.SettingsNavigationService

class SettingsNavigationServiceImplementation(
    private val context: Context
) : SettingsNavigationService {
    override fun getOverviewIntent(): Intent {
        return Intent(context, SettingsActivity::class.java)
    }

    override fun getChangeEmailIntent(): Intent {
        return Intent(context, ChangeEmailActivity::class.java)
    }
}
