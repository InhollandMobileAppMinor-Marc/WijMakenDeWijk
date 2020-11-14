package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
