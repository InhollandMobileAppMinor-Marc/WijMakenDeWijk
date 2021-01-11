package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import nl.woonwaard.wij_maken_de_wijk.ui.settings.databinding.ActivityChangePasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : AppCompatActivity() {
    private val viewModel by viewModel<ChangePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.changePasswordButton.setOnClickListener {
            hideKeyboard()

            if(!viewModel.isPasswordCorrect(binding.content.newPasswordInputField.text.toString())) {
                binding.content.newPasswordField.error = getString(R.string.password_too_weak)
            } else {
                viewModel.changePassword(
                    binding.content.emailInputField.text.toString(),
                    binding.content.oldPasswordInputField.text.toString(),
                    binding.content.newPasswordInputField.text.toString()
                )
            }
        }

        binding.content.newPasswordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.newPasswordField.error = null
        }

        binding.content.emailInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
            binding.content.oldPasswordField.error = null
        }

        binding.content.oldPasswordInputField.doOnTextChanged { _, _, _, _ ->
            binding.content.emailField.error = null
            binding.content.oldPasswordField.error = null
        }

        viewModel.isLoading.observe(this) {
            hideKeyboard()
            binding.content.changePasswordButton.isEnabled = !it
        }

        viewModel.changePasswordResult.observe(this) {
            if(it != null) viewModel.changePasswordResult.postValue(null)

            when(it) {
                true -> finish()
                false -> {
                    binding.content.emailField.error = getString(R.string.invalid_login)
                    binding.content.oldPasswordField.error = getString(R.string.invalid_login)
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.clearRunningJobs()
        super.onDestroy()
    }
}
