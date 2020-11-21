package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.ActivityPinboardOverviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinboardOverviewActivity : AppCompatActivity() {
    private val viewModel by viewModel<PinboardOverviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPinboardOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val adapter = PinboardAdapter(viewModel.posts)

        // TODO: binding.content.recyclerView.setHasFixedSize(true)
        binding.content.recyclerView.adapter = adapter

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPosts()
        }

        viewModel.isLoading.observe(this) {
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.posts.observe(this) {
            adapter.notifyDataSetChanged()
        }
    }
}
