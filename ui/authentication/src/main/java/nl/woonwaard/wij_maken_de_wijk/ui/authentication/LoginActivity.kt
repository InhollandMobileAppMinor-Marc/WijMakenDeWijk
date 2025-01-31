package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.MainNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.databinding.ActivityLoginBinding
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModel<LoginViewModel>()

    private val mainNavigationService by inject<MainNavigationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.login.setOnClickListener {
            hideKeyboard()

            viewModel.login(
                binding.content.emailInputField.text.toString(),
                binding.content.passwordInputField.text.toString()
            ) {
                runOnUiThread {
                    binding.content.emailField.error = getString(R.string.invalid_login)
                    binding.content.passwordField.error = getString(R.string.invalid_login)
                }
            }
        }

        binding.content.emailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
            binding.content.passwordField.error = null
        }

        binding.content.passwordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
            binding.content.passwordField.error = null
        }

        viewModel.isLoading.observe(this) {
            binding.content.login.isEnabled = !it
        }

        viewModel.isLoggedIn.observe(this) {
            if(it) {
                startActivity(mainNavigationService.getHomeScreenIntent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
        }
    }
}
