package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.MainNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.databinding.ActivityRegistrationBinding
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModel<RegistrationViewModel>()

    private val mainNavigationService by inject<MainNavigationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.signup.setOnClickListener {
            hideKeyboard()

            val correctnessChecks = setOf(
                Triple(binding.content.codeInputField, viewModel::isCodeCorrect) {
                    binding.content.codeField.error = getString(R.string.incorrect_code)
                },
                Triple(binding.content.nameInputField, viewModel::isNameCorrect) {
                    binding.content.nameField.error = getString(R.string.invalid_name)
                },
                Triple(binding.content.emailInputField, viewModel::isEmailCorrect) {
                    binding.content.emailField.error = getString(R.string.invalid_email)
                },
                Triple(binding.content.passwordInputField, viewModel::isPasswordCorrect) {
                    binding.content.passwordField.error = getString(R.string.password_too_weak)
                },
                Triple(binding.content.repeatPasswordInputField, { repeatedPassword ->
                    repeatedPassword == binding.content.passwordInputField.text.toString()
                }, {
                    binding.content.repeatPasswordField.error = getString(R.string.not_identical)
                })
            )

            for ((field, checker, errorMarker) in correctnessChecks) {
                if(!checker(field.text.toString()))
                    errorMarker()
            }

            if(correctnessChecks.all { it.second(it.first.text.toString()) }) {
                val (code, name, email, password) = correctnessChecks.map { it.first.text.toString() }
                viewModel.signup(code, name, email, password)
            }
        }

        binding.content.codeInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.codeField.error = null
        }

        binding.content.nameInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.nameField.error = null
        }

        binding.content.emailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
        }

        binding.content.passwordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.passwordField.error = null
            binding.content.repeatPasswordField.error = null
        }

        binding.content.repeatPasswordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.passwordField.error = null
            binding.content.repeatPasswordField.error = null
        }

        viewModel.isLoading.observe(this) {
            binding.content.signup.isEnabled = !it
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
