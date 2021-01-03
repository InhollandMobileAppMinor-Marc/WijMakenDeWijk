package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.RepairsNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.WmdwCustomTabsStyle
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.customTabsSession
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityRepairsOverviewBinding
import org.koin.android.ext.android.inject

class RepairsOverviewActivity : AppCompatActivity() {
    private val customTabsSession by customTabsSession()

    private val repairsNavigationService by inject<RepairsNavigationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRepairsOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.otherRepairsButton.setOnClickListener {
            openInBrowser(WOONWAARD_REPAIR_URL)
        }

        binding.content.centralHeatingButton.setOnClickListener {
            startActivity(repairsNavigationService.getCentralHeatingIntent())
        }

        binding.content.glassButton.setOnClickListener {
            startActivity(repairsNavigationService.getGlassIntent())
        }

        binding.content.liftButton.setOnClickListener {
            startActivity(repairsNavigationService.getLiftIntent())
        }

        binding.content.cleaningButton.setOnClickListener {
            startActivity(repairsNavigationService.getCleaningIntent())
        }

        binding.content.greenMaintenanceButton.setOnClickListener {
            startActivity(repairsNavigationService.getGreenMaintenanceIntent())
        }

        binding.content.emergencyRepairsButton.setOnClickListener {
            startActivity(repairsNavigationService.getEmergencyIntent())
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_REPAIR_URL.toUri(), null, null)
        }
    }

    private fun openInBrowser(url: String) {
        WmdwCustomTabsStyle
                .createCustomTabsSessionWithWmdwStyle(this, customTabsSession.value)
                .launchUrl(this, url.toUri())
    }

    companion object {
        const val WOONWAARD_REPAIR_URL = "https://www.woonwaard.nl/voor-huurders/reparaties-en-onderhoud/reparatieverzoek"
    }
}
