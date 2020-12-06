package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.view.View
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsActivity.Companion.navigateToPostDetails
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityCreatePostBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePostActivity : AppCompatActivity() {
    private val viewModel by viewModel<CreatePostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.submit.setOnClickListener {
            hideKeyboard()

            val type = when(binding.content.radioGroup.checkedRadioButtonId) {
                binding.content.serviceRadioButton.id -> "SERVICE"
                binding.content.gatheringRadioButton.id -> "GATHERING"
                binding.content.sustainabilityRadioButton.id -> "SUSTAINABILITY"
                else -> return@setOnClickListener
            }

            viewModel.createPost(
                binding.content.titleInputField.text.toString(),
                type,
                binding.content.messageInputField.text.toString()
            )
        }

        viewModel.isLoading.observe(this) {
            binding.content.submit.isEnabled = !it
        }

        viewModel.createdPost.observe(this) {
            if(it !== null) {
                navigateToPostDetails(it)
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
        fun Context.navigateToPostCreation() {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
    }
}
