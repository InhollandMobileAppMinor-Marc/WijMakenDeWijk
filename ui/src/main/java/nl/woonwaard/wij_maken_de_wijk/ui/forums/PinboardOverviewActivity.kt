package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.ui.R
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPinboardOverviewBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PinboardListItemBinding
import nl.woonwaard.wij_maken_de_wijk.ui.forums.CreatePostActivity.Companion.navigateToPostCreation
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsActivity.Companion.navigateToPostDetails
import nl.woonwaard.wij_maken_de_wijk.ui.utils.GenericViewBindingRecyclerViewAdapter
import nl.woonwaard.wij_maken_de_wijk.ui.utils.context
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinboardOverviewActivity : AppCompatActivity() {
    private val viewModel by viewModel<PinboardOverviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPinboardOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = GenericViewBindingRecyclerViewAdapter.create(
            viewModel.posts as LiveData<Collection<Post>>,
            PinboardListItemBinding::inflate
        ) { binding, post, _ ->
            binding.cardView.setOnClickListener {
                it.context.navigateToPostDetails(post)
            }

            binding.title.text = post.title

            binding.content.text =
                if(post.deleted) binding.context.getString(R.string.deleted)
                else post.body.replace('\n', ' ')

            binding.user.text =
                if(post.author.deleted) binding.context.getString(R.string.deleted)
                else post.author.nameWithLocation

            binding.category.visibility = if(viewModel.singleCategory) View.GONE else View.VISIBLE
            binding.category.setText(when(post.category) {
                "SERVICE" -> R.string.service
                "GATHERING" -> R.string.gathering
                "SUSTAINABILITY" -> R.string.sustainability
                "IDEA" -> R.string.idea
                else -> R.string.unknown
            })

            binding.time.text = DateUtils.getRelativeTimeSpanString(
                post.timestamp.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        }

        binding.content.recyclerView.setHasFixedSize(true)
        binding.content.recyclerView.adapter = adapter

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadPosts()
        }

        binding.createPostFab.setOnClickListener {
            navigateToPostCreation()
        }

        viewModel.isLoading.observe(this) {
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.posts.observe(this) {
            adapter.notifyDataSetChanged()
        }

        viewModel.loadPosts(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.asList())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent != null)
            viewModel.loadPosts(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.asList())
    }

    override fun onStart() {
        super.onStart()
        viewModel.reloadPosts()
    }

    companion object {
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"

        fun Context.navigateToPinboardOverview(categories: List<String>? = null) {
            val intent = Intent(this, PinboardOverviewActivity::class.java)
            if(categories != null)
                intent.putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
            startActivity(intent)
        }
    }
}
