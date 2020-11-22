package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.PinboardOverviewActivity.Companion.navigateToPinboardOverview
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
            startActivity(Intent(Intent.ACTION_VIEW, "https://www.woonwaard.nl/voor-huurders/reparaties-en-onderhoud/reparatieverzoek".toUri()))
        }

        binding.content.neighborhoodMediationButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, "https://www.woonwaard.nl/overlast".toUri()))
        }
    }
}
