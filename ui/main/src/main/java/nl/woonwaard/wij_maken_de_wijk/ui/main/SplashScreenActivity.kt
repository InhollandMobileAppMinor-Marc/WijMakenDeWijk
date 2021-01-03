package nl.woonwaard.wij_maken_de_wijk.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.woonwaard.wij_maken_de_wijk.domain.models.ApiStatus
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.NavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.core.terminateApplication
import nl.woonwaard.wij_maken_de_wijk.ui.main.databinding.ActivitySplashScreenBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModel<SplashScreenViewModel>()

    private val navigationService by inject<NavigationService>()

    private val notificationPreferences by lazy {
        getSharedPreferences("notifications", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.ensureCorrectNotificationConfiguration(notificationPreferences.getBoolean(CHECK_FOR_NOTIFICATIONS, true))

        viewModel.apiStatus.observe(this) {
            when (it) {
                is ApiStatus.ConnectionError -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.no_connection))
                        .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                            viewModel.onCancelled()
                        }
                        .setPositiveButton(resources.getString(R.string.try_again)) { _, _ ->
                            viewModel.onSplashScreenShown()
                        }
                        .show()
                    return@observe
                }
                is ApiStatus.LoggedOut -> startActivity(navigationService.authentication.getLoginIntent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                is ApiStatus.LoggedIn -> startActivity(navigationService.main.getHomeScreenIntent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

            viewModel.onDismissed()
        }

        viewModel.shouldShowSplashScreen.observe(this) {
            when (it) {
                SplashScreenViewModel.SplashScreenState.SHOULD_SHOW -> {
                    viewModel.onSplashScreenShown()
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

    companion object {
        const val CHECK_FOR_NOTIFICATIONS = "CHECK_FOR_NOTIFICATIONS"
    }
}
