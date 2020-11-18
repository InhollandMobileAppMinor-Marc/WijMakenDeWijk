package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
