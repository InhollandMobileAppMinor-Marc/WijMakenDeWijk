package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutManagerCompat
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.AuthenticationNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.settings.databinding.ActivitySettingsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()

    private val authenticationNavigationService by inject<AuthenticationNavigationService>()

    private val notificationPreferences by lazy {
        getSharedPreferences("notifications", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.logout.setOnClickListener {
            viewModel.logout()
        }

        binding.content.deleteAccount.setOnClickListener {
            viewModel.deleteAccount()
        }

        viewModel.isLoggedIn.observe(this) {
            if(!it) {
                ShortcutManagerCompat.removeAllDynamicShortcuts(this)
                startActivity(authenticationNavigationService.getLoginIntent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
        }

        binding.content.checkForNotificationsToggle.isChecked = notificationPreferences.getBoolean(CHECK_FOR_NOTIFICATIONS, true)

        binding.content.checkForNotificationsToggle.setOnCheckedChangeListener { _, isChecked ->
            viewModel.ensureCorrectNotificationConfiguration(isChecked)
            notificationPreferences.edit().putBoolean(CHECK_FOR_NOTIFICATIONS, isChecked).apply()
        }
    }

    companion object {
        const val CHECK_FOR_NOTIFICATIONS = "CHECK_FOR_NOTIFICATIONS"
    }
}
