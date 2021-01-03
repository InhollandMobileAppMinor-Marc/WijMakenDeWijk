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

    override fun getCentralHeatingIntent(): Intent {
        return Intent(context, CentralHeatingActivity::class.java)
    }

    override fun getGlassIntent(): Intent {
        return Intent(context, GlassActivity::class.java)
    }

    override fun getLiftIntent(): Intent {
        return Intent(context, LiftActivity::class.java)
    }

    override fun getCleaningIntent(): Intent {
        return Intent(context, CleaningActivity::class.java)
    }

    override fun getGreenMaintenanceIntent(): Intent {
        return Intent(context, GreenMaintenanceActivity::class.java)
    }

    override fun getEmergencyIntent(): Intent {
        return Intent(context, EmergencyActivity::class.java)
    }
}
