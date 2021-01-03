package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.WmdwCustomTabsStyle
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.customTabsSession
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityGreenMaintenanceBinding

class GreenMaintenanceActivity : AppCompatActivity() {
    private val customTabsSession by customTabsSession()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGreenMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.moreInfoButton.setOnClickListener {
            openInBrowser(WOONWAARD_GREEN_MAINTENANCE_URL)
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_GREEN_MAINTENANCE_URL.toUri(), null, null)
        }
    }

    private fun openInBrowser(url: String) {
        WmdwCustomTabsStyle
                .createCustomTabsSessionWithWmdwStyle(this, customTabsSession.value)
                .launchUrl(this, url.toUri())
    }

    companion object {
        const val WOONWAARD_GREEN_MAINTENANCE_URL = "https://www.woonwaard.nl/in-de-wijk/leefomgeving/meepraten"
    }
}
