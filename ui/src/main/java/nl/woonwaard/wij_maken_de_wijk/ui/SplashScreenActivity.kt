package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.woonwaard.wij_maken_de_wijk.ui.LoginActivity.Companion.navigateToLogin
import nl.woonwaard.wij_maken_de_wijk.ui.MainActivity.Companion.navigateToMain
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivitySplashScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModel<SplashScreenViewModel>()

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
                    if(viewModel.isLoggedIn) navigateToMain()
                    else navigateToLogin()

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
