package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.ui.MainActivity.Companion.navigateToMain
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

            viewModel.signup(
                binding.content.codeInputField.text.toString(),
                binding.content.nameInputField.text.toString(),
                binding.content.emailInputField.text.toString(),
                binding.content.passwordInputField.text.toString()
            )
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
