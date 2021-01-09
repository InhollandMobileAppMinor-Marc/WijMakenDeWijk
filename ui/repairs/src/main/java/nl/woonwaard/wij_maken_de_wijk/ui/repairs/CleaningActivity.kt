package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityCleaningBinding

class CleaningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCleaningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
