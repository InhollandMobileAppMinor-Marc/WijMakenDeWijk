package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.ui.MainNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.R
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityRegistrationBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModel<RegistrationViewModel>()

    private val mainNavigationService by inject<MainNavigationServiceImplementation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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
                }
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
        }

        viewModel.isLoading.observe(this) {
            binding.content.signup.isEnabled = !it
        }

        viewModel.isLoggedIn.observe(this) {
            if(it) {
                startActivity(mainNavigationService.getHomeScreenIntent())
                finish()
            }
        }
    }

    private fun hideKeyboard() {
        currentFocus?.clearFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
