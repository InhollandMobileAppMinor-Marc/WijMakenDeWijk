package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
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

        val adapter = PostDetailsAdapter(viewModel.post, viewModel.comments)

        binding.content.recyclerView.adapter = adapter

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadPostData()
        }

        binding.content.sendButton.setOnClickListener {
            viewModel.addComment(binding.content.commentInputField.text.toString())
            binding.content.commentInputField.setText("")
        }

        viewModel.isLoading.observe(this) {
            binding.content.sendButton.isEnabled = !it
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.post.observe(this) {
            if(it != null) {
                title = it.title
            }
            adapter.notifyDataSetChanged()
        }

        viewModel.comments.observe(this) {
            adapter.notifyDataSetChanged()
        }

        val post = intent.getSerializableExtra(EXTRA_POST) as? Post
        if(post != null) viewModel.loadPostData(post)
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
