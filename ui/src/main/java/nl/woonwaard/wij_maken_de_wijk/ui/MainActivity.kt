package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityMainBinding
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PinboardOverviewActivity.Companion.navigateToPinboardOverview
import nl.woonwaard.wij_maken_de_wijk.ui.settings.SettingsActivity.Companion.navigateToSettings
import nl.woonwaard.wij_maken_de_wijk.ui.utils.CustomTabsHelper
import nl.woonwaard.wij_maken_de_wijk.ui.utils.terminateApplication

class MainActivity : AppCompatActivity() {
    private val customTabsSession by lazy {
        CustomTabsHelper.createSession(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.pinboardButton.setOnClickListener {
            navigateToPinboardOverview()
        }

        binding.content.repairsButton.setOnClickListener {
            openInBrowser(WOONWAARD_REPAIR_URL)
        }

        binding.content.neighborhoodMediationButton.setOnClickListener {
            openInBrowser(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL)
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_REPAIR_URL.toUri(), null, null)
            it?.mayLaunchUrl(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL.toUri(), null, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_open_settings -> navigateToSettings()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onBackPressed() {
        terminateApplication()
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

        const val WOONWAARD_NEIGHBORHOOD_MEDIATION_URL = "https://www.woonwaard.nl/overlast"

        fun Context.navigateToMain() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
