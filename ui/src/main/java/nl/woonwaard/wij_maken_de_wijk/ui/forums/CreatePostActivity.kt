package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityCreatePostBinding
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsActivity.Companion.navigateToPostDetails
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

            val type = (
                if(viewModel.singleCategory) viewModel.categories.value?.firstOrNull()
                else when(binding.content.radioGroup.checkedRadioButtonId) {
                    binding.content.serviceRadioButton.id -> PostCategory.SERVICE
                    binding.content.gatheringRadioButton.id -> PostCategory.GATHERING
                    binding.content.sustainabilityRadioButton.id -> PostCategory.SUSTAINABILITY
                    binding.content.ideaRadioButton.id -> PostCategory.IDEA
                    else -> null
                }
            ) ?: PostCategory.OTHER

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

        viewModel.categories.observe(this) {
            val typeVisibility = if(it?.size == 1) View.GONE else View.VISIBLE
            binding.content.radioGroup.visibility = typeVisibility
            binding.content.typeText.visibility = typeVisibility

            binding.content.gatheringRadioButton.visibility =
                if(it != null && PostCategory.GATHERING in it) View.VISIBLE
                else View.GONE

            binding.content.serviceRadioButton.visibility =
                if(it != null && PostCategory.SERVICE in it) View.VISIBLE
                else View.GONE

            binding.content.sustainabilityRadioButton.visibility =
                if(it != null && PostCategory.SUSTAINABILITY in it) View.VISIBLE
                else View.GONE

            binding.content.ideaRadioButton.visibility =
                if(it != null && PostCategory.IDEA in it) View.VISIBLE
                else View.GONE
        }

        viewModel.categories.postValue(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.toSet())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent != null)
            viewModel.categories.postValue(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.toSet())
    }

    private fun hideKeyboard() {
        currentFocus?.clearFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"

        fun Context.navigateToPostCreation(categories: Set<String>? = null) {
            val intent = Intent(this, CreatePostActivity::class.java)
            if(categories != null)
                intent.putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
            startActivity(intent)
        }
    }
}
