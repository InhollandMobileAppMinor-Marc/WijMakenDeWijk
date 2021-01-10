package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.utils.getSerializableArrayExtra
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import nl.woonwaard.wij_maken_de_wijk.ui.core.terminateApplication
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_COMMENTS
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_FROM_NOTIFICATION
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_POST
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_POST_ID
import nl.woonwaard.wij_maken_de_wijk.ui.forums.databinding.ActivityPostDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<PostDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)

        val adapter = PostDetailsAdapter(
            viewModel.post,
            viewModel.comments,
            viewModel::deleteComment,
            viewModel::reportComment,
            viewModel.currentUser
        )

        binding.content.recyclerView.adapter = adapter

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadPostData()
        }

        binding.content.sendButton.setOnClickListener {
            hideKeyboard()
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

        viewModel.isFromNotification = intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)

        if(!viewModel.isFromNotification) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post_details, menu)
        menu?.findItem(R.id.action_delete_post)?.isEnabled = viewModel.canBeDeleted
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_post -> viewModel.deletePost()
            R.id.action_report_post -> viewModel.reportPost()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onBackPressed() {
        if(viewModel.isFromNotification) terminateApplication()
        else super.onBackPressed()
    }
}
