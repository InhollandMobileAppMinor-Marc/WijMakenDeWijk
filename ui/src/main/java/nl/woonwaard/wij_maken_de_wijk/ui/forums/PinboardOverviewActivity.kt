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
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.ForumsNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.R
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPinboardOverviewBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PinboardListItemBinding
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation.Companion.EXTRA_CATEGORIES
import nl.woonwaard.wij_maken_de_wijk.ui.utils.GenericViewBindingRecyclerViewAdapter
import nl.woonwaard.wij_maken_de_wijk.ui.utils.context
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinboardOverviewActivity : AppCompatActivity() {
    private val viewModel by viewModel<PinboardOverviewViewModel>()

    private val forumsNavigationService by inject<ForumsNavigationService>()

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
                it.context.startActivity(forumsNavigationService.getPostDetailsIntent(post))
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
                PostCategory.SERVICE -> R.string.service
                PostCategory.GATHERING -> R.string.gathering
                PostCategory.SUSTAINABILITY -> R.string.sustainability
                PostCategory.IDEA -> R.string.idea
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
            viewModel.loadPosts()
        }

        binding.createPostFab.setOnClickListener {
            startActivity(forumsNavigationService.getCreatePostIntent(viewModel.categories.value))
        }

        viewModel.isLoading.observe(this) {
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.posts.observe(this) {
            adapter.notifyDataSetChanged()
        }

        viewModel.categories.observe(this) {
            title = getString(if(it == setOf(PostCategory.IDEA)) R.string.ideas else R.string.pinboard)
        }

        viewModel.changeCategories(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.toSet())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent != null)
            viewModel.changeCategories(intent.getStringArrayExtra(EXTRA_CATEGORIES)?.toSet())
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadPosts()
    }
}
