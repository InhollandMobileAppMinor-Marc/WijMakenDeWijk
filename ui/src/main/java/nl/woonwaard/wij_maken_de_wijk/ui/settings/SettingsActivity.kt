package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.LoginActivity.Companion.navigateToLogin
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.isLoggedIn.observe(this) {
            if(!it) {
                navigateToLogin()
                finish()
            }
        }
    }

    companion object {
        fun Context.navigateToSettings() {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
