package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.ui.MainActivity.Companion.navigateToMain
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.LoginActivity.Companion.navigateToLogin
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivitySplashScreenBinding
import nl.woonwaard.wij_maken_de_wijk.ui.settings.SettingsActivity
import nl.woonwaard.wij_maken_de_wijk.ui.utils.terminateApplication
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModel<SplashScreenViewModel>()

    private val notificationPreferences by lazy {
        getSharedPreferences("notifications", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.ensureCorrectNotificationConfiguration(notificationPreferences.getBoolean(
            SettingsActivity.CHECK_FOR_NOTIFICATIONS, true))

        viewModel.shouldShowSplashScreen.observe(this) {
            when (it) {
                SplashScreenViewModel.SplashScreenState.SHOULD_SHOW -> {
                    viewModel.onSplashScreenShown()
                }
                SplashScreenViewModel.SplashScreenState.SHOULD_DISMISS -> {
                    if(viewModel.isLoggedIn) navigateToMain()
                    else navigateToLogin()

                    viewModel.onDismissed()
                }
                SplashScreenViewModel.SplashScreenState.DISMISSED -> {
                    finish()
                }
                SplashScreenViewModel.SplashScreenState.CANCELLED -> {
                    terminateApplication()
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.onCancelled()
    }
}
