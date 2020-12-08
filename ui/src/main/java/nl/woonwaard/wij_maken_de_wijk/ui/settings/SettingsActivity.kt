package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.view.View
import nl.woonwaard.wij_maken_de_wijk.ui.MainActivity.Companion.navigateToMain
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.LoginActivity.Companion.navigateToLogin
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.RegistrationActivity.Companion.navigateToRegistration
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityLoginBinding
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
