package nl.woonwaard.wij_maken_de_wijk.ui.neighborhood_mediation

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.NeighborhoodMediationNavigationService
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.SettingsNavigationService

class NeighborhoodMediationNavigationServiceImplementation(
    val context: Context
) : NeighborhoodMediationNavigationService {
    override fun getOverviewIntent(): Intent {
        return Intent(context, NeighborhoodMediationActivity::class.java)
    }
}
