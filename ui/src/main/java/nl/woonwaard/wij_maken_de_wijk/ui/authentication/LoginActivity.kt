package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.ui.MainActivity.Companion.navigateToMain
import nl.woonwaard.wij_maken_de_wijk.ui.R
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.RegistrationActivity.Companion.navigateToRegistration
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityLoginBinding
import nl.woonwaard.wij_maken_de_wijk.ui.utils.terminateApplication
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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

        binding.content.signup.setOnClickListener {
            navigateToRegistration()
        }

        viewModel.isLoading.observe(this) {
            binding.content.login.isEnabled = !it
            binding.content.signup.isEnabled = !it
        }

        viewModel.isLoggedIn.observe(this) {
            if(it) {
                navigateToMain()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        terminateApplication()
    }

    private fun hideKeyboard() {
        currentFocus?.clearFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        fun Context.navigateToLogin() {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
