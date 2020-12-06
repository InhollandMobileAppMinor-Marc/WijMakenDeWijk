package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PinboardOverviewActivity.Companion.navigateToPinboardOverview
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityMainBinding

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

    companion object {
        const val WOONWAARD_REPAIR_URL = "https://www.woonwaard.nl/voor-huurders/reparaties-en-onderhoud/reparatieverzoek"

        const val WOONWAARD_NEIGHBORHOOD_MEDIATION_URL = "https://www.woonwaard.nl/overlast"

        fun Context.navigateToMain() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
