package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import nl.woonwaard.wij_maken_de_wijk.ui.settings.databinding.ActivityChangeEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeEmailActivity : AppCompatActivity() {
    private val viewModel by viewModel<ChangeEmailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.changeEmailButton.setOnClickListener {
            hideKeyboard()

            if(!viewModel.isEmailCorrect(binding.content.newEmailInputField.text.toString())) {
                binding.content.newEmailField.error = getString(R.string.invalid_email)
            } else {
                viewModel.changeEmail(
                    binding.content.newEmailInputField.text.toString(),
                    binding.content.oldEmailInputField.text.toString(),
                    binding.content.passwordInputField.text.toString()
                )
            }
        }

        binding.content.newEmailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.newEmailField.error = null
        }

        binding.content.oldEmailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.oldEmailField.error = null
            binding.content.passwordField.error = null
        }

        binding.content.passwordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.oldEmailField.error = null
            binding.content.passwordField.error = null
        }

        viewModel.isLoading.observe(this) {
            hideKeyboard()
            binding.content.changeEmailButton.isEnabled = !it
        }

        viewModel.changeEmailResult.observe(this) {
            if(it != null) viewModel.changeEmailResult.postValue(null)

            when(it) {
                true -> finish()
                false -> {
                    binding.content.oldEmailField.error = getString(R.string.invalid_login)
                    binding.content.passwordField.error = getString(R.string.invalid_login)
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }
}
