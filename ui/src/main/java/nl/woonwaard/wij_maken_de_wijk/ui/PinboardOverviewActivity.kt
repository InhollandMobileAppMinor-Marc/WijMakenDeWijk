package nl.woonwaard.wij_maken_de_wijk.ui

import android.content.Context
import android.content.Intent
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = PinboardAdapter(viewModel.posts)

        binding.content.recyclerView.setHasFixedSize(true)
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

    companion object {
        fun Context.navigateToPinboardOverview() {
            startActivity(Intent(this, PinboardOverviewActivity::class.java))
        }
    }
}
