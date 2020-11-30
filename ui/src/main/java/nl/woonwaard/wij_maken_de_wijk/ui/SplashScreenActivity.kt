package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import nl.woonwaard.wij_maken_de_wijk.ui.LoginActivity.Companion.navigateToLogin
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.shouldShowSplashScreen.observe(this) {
            when (it) {
                SplashScreenViewModel.SplashScreenState.SHOULD_SHOW -> {
                    viewModel.onSplashScreenShown()
                }
                SplashScreenViewModel.SplashScreenState.SHOULD_DISMISS -> {
                    navigateToLogin()
                    viewModel.onDismissed()
                }
                SplashScreenViewModel.SplashScreenState.DISMISSED -> {
                    finish()
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }
}
