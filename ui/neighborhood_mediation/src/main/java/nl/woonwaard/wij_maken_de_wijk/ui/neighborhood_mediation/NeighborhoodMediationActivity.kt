package nl.woonwaard.wij_maken_de_wijk.ui.neighborhood_mediation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.WmdwCustomTabsStyle
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.customTabsSession
import nl.woonwaard.wij_maken_de_wijk.ui.neighborhood_mediation.databinding.ActivityNeighborhoodMediationBinding

class NeighborhoodMediationActivity : AppCompatActivity() {
    private val customTabsSession by customTabsSession()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNeighborhoodMediationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.openMediationWebsiteButton.setOnClickListener {
            openInBrowser(NEIGHBORHOOD_MEDIATION_WEBSITE_URL)
        }

        binding.content.contactWoonwaardButton.setOnClickListener {
            openInBrowser(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL)
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL.toUri(), null, null)
            it?.mayLaunchUrl(NEIGHBORHOOD_MEDIATION_WEBSITE_URL.toUri(), null, null)
        }
    }

    private fun openInBrowser(url: String) {
        WmdwCustomTabsStyle
            .createCustomTabsSessionWithWmdwStyle(this, customTabsSession.value)
            .launchUrl(this, url.toUri())
    }

    companion object {
        const val WOONWAARD_NEIGHBORHOOD_MEDIATION_URL = "https://www.woonwaard.nl/overlast/burenoverlast-melden/"
        const val NEIGHBORHOOD_MEDIATION_WEBSITE_URL = "https://www.debemiddelingskamer.nl/wat-kunt-u-zelf-doen/"
    }
}
