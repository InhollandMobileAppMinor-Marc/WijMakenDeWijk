package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PinboardOverviewActivity.Companion.navigateToPinboardOverview
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityMainBinding
import nl.woonwaard.wij_maken_de_wijk.ui.settings.SettingsActivity.Companion.navigateToSettings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.pinboardButton.setOnClickListener {
            navigateToPinboardOverview()
        }

        binding.content.repairsButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, WOONWAARD_REPAIR_URL.toUri()))
        }

        binding.content.neighborhoodMediationButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, WOONWAARD_NEIGHBORHOOD_MEDIATION_URL.toUri()))
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
        finishAndRemoveTask()
    }

    companion object {
        const val WOONWAARD_REPAIR_URL = "https://www.woonwaard.nl/voor-huurders/reparaties-en-onderhoud/reparatieverzoek"

        const val WOONWAARD_NEIGHBORHOOD_MEDIATION_URL = "https://www.woonwaard.nl/overlast"

        fun Context.navigateToMain() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
