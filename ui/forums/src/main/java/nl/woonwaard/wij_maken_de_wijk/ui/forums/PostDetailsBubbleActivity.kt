package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.User
import nl.woonwaard.wij_maken_de_wijk.domain.utils.getSerializableArrayExtra
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import nl.woonwaard.wij_maken_de_wijk.ui.core.terminateApplication
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_COMMENTS
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_FROM_NOTIFICATION
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_POST
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_POST_ID
import nl.woonwaard.wij_maken_de_wijk.ui.forums.databinding.ActivityPostDetailsBubbleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailsBubbleActivity : AppCompatActivity() {
    private val viewModel by viewModel<PostDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostDetailsBubbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        viewModel.isFromNotification = intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)

        val currentUser = intent.getSerializableExtra(ForumsNavigationServiceImplementation.EXTRA_CURRENT_USER) as? User

        if(currentUser != null)
            viewModel.setCurrentUser(currentUser)

        val post = intent.getSerializableExtra(EXTRA_POST) as? Post
        if(post != null) {
            val comments = intent.getSerializableArrayExtra(EXTRA_COMMENTS)
            if(comments.isNotEmpty()) {
                viewModel.loadPostData(post, comments.mapNotNull {
                    it as? Comment
                }.toSet())
            } else viewModel.loadPostData(post)
        } else {
            val postId = intent.getStringExtra(EXTRA_POST_ID)
            if(postId != null) viewModel.loadPostData(postId)
        }

        val adapter = PostDetailsAdapter(
            viewModel.post,
            viewModel.comments,
            currentUser = viewModel.currentUser,
            showTitleInHeader = true
        )

        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadPostData()
        }

        binding.sendButton.setOnClickListener {
            hideKeyboard()
            viewModel.addComment(binding.commentInputField.text.toString())
            binding.commentInputField.setText("")
        }

        viewModel.isLoading.observe(this) {
            binding.sendButton.isEnabled = !it
            binding.swipeRefreshLayout.isRefreshing = it
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
    }

    override fun onBackPressed() {
        if(viewModel.isFromNotification) terminateApplication()
        else super.onBackPressed()
    }
}
