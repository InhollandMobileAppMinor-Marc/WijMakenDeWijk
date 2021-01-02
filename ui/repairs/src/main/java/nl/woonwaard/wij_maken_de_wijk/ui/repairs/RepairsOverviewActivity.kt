package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.RepairsNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.core.customTabsSession
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

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_REPAIR_URL.toUri(), null, null)
        }
    }

    private fun openInBrowser(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .build()
            )
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)

        val session = customTabsSession.value
        if(session != null) customTabsBuilder.setSession(session)

        customTabsBuilder.build().launchUrl(this, url.toUri())
    }

    companion object {
        const val WOONWAARD_REPAIR_URL = "https://www.woonwaard.nl/voor-huurders/reparaties-en-onderhoud/reparatieverzoek"
    }
}
