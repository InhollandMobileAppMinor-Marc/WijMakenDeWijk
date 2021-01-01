package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.RepairsNavigationService

class RepairsNavigationServiceImplementation(
    val context: Context
) : RepairsNavigationService {
    override fun getOverviewIntent(): Intent {
        return Intent(context, RepairsOverviewActivity::class.java)
    }
}
