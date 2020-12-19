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
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityRegistrationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModel<RegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.signup.setOnClickListener {
            hideKeyboard()

            val code = binding.content.codeInputField.text.toString()
            val isCodeCorrect = viewModel.isCodeCorrect(code)

            val email = binding.content.emailInputField.text.toString()
            val isEmailCorrect = viewModel.isEmailCorrect(email)

            if(!isCodeCorrect) {
                binding.content.codeField.error = getString(R.string.incorrect_code)
            }

            if(!isEmailCorrect) {
                binding.content.emailField.error = getString(R.string.invalid_email)
            }

            if(isCodeCorrect && isEmailCorrect) {
                viewModel.signup(
                    code,
                    binding.content.nameInputField.text.toString(),
                    email,
                    binding.content.passwordInputField.text.toString()
                )
            }
        }

        binding.content.codeInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.codeField.error = null
        }

        binding.content.emailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
        }

        viewModel.isLoading.observe(this) {
            binding.content.signup.isEnabled = !it
        }

        viewModel.isLoggedIn.observe(this) {
            if(it) {
                navigateToMain()
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

    companion object {
        fun Context.navigateToRegistration() {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
