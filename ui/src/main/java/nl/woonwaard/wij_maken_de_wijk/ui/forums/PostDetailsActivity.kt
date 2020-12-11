package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPostDetailsBinding
import nl.woonwaard.wij_maken_de_wijk.ui.utils.terminateApplication
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<PostDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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

        viewModel.isFromNotification = intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, true)

        if(!viewModel.isFromNotification) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val post = intent.getSerializableExtra(EXTRA_POST) as? Post
        if(post != null) {
            val comments = intent.getIntExtra(EXTRA_COMMENTS, 0)
            if(comments > 0) {
                viewModel.loadPostData(post, (0 until comments).mapNotNull {
                    intent.getSerializableExtra("${EXTRA_COMMENTS}_$it") as? Comment
                }.toSet())
            } else viewModel.loadPostData(post)
        } else {
            val postId = intent.getStringExtra(EXTRA_POST_ID)
            if(postId != null) viewModel.loadPostData(postId)
        }
    }

    override fun onBackPressed() {
        if(viewModel.isFromNotification) terminateApplication()
        else super.onBackPressed()
    }

    companion object {
        const val EXTRA_POST = "EXTRA_POST"
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val EXTRA_COMMENTS = "EXTRA_COMMENTS"
        const val EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION"

        fun Context.navigateToPostDetails(post: Post) {
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra(EXTRA_POST, post)
            startActivity(intent)
        }
    }
}
