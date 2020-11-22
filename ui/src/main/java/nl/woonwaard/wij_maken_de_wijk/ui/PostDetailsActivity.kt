package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPinboardOverviewBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPostDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<PostDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPostData()
        }

        viewModel.isLoading.observe(this) {
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.post.observe(this) {
            if(it != null) {
                title = it.title
                binding.content.body.text = it.body
            }
        }

        val post = intent.getSerializableExtra(EXTRA_POST) as? Post
        viewModel.post.postValue(post)
    }

    companion object {
        const val EXTRA_POST = "EXTRA_POST"

        fun Context.navigateToPostDetails(post: Post) {
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra(EXTRA_POST, post)
            startActivity(intent)
        }
    }
}
