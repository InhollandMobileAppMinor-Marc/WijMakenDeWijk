package nl.woonwaard.wij_maken_de_wijk.ui.main

import android.os.Bundle
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.NavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.WmdwCustomTabsStyle
import nl.woonwaard.wij_maken_de_wijk.ui.core.custom_tabs.customTabsSession
import nl.woonwaard.wij_maken_de_wijk.ui.core.terminateApplication
import nl.woonwaard.wij_maken_de_wijk.ui.main.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    private val customTabsSession by customTabsSession()

    private val viewModel by viewModel<MainViewModel>()

    private val navigationService by inject<NavigationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!viewModel.isLoggedIn) {
            startActivity(navigationService.authentication.getInviteScreenIntent())
            finish()
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.pinboardButton.setOnClickListener {
            startActivity(
                navigationService.forums.getOverviewIntent(setOf(
                    PostCategory.SERVICE,
                    PostCategory.GATHERING,
                    PostCategory.SUSTAINABILITY,
                    PostCategory.OTHER
                ))
            )
        }

        binding.content.repairsButton.setOnClickListener {
            startActivity(navigationService.repairs.getOverviewIntent())
        }

        binding.content.neighborhoodMediationButton.setOnClickListener {
            openInBrowser(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL)
        }

        binding.content.ideasButton.setOnClickListener {
            startActivity(navigationService.forums.getOverviewIntent(setOf(PostCategory.IDEA)))
        }

        viewModel.notice.observe(this) {
            binding.content.noticeCardView.visibility = if(it == null) View.GONE else View.VISIBLE

            if(it != null) {
                binding.content.noticeTitle.text = it.title
                binding.content.noticeContent.text = DateUtils.getRelativeTimeSpanString(
                        it.timestamp.time,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS
                ).toString() + " - " + it.body
                binding.content.noticeCardView.setOnClickListener { _ ->
                    MaterialAlertDialogBuilder(this)
                            .setTitle(it.title)
                            .setMessage(DateUtils.getRelativeTimeSpanString(
                                    it.timestamp.time,
                                    System.currentTimeMillis(),
                                    DateUtils.MINUTE_IN_MILLIS
                            ).toString() + "\n\n" + it.body)
                            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            binding.content.swipeRefreshLayout.isRefreshing = it
        }

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadNotice()
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(WOONWAARD_NEIGHBORHOOD_MEDIATION_URL.toUri(), null, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_open_settings -> startActivity(navigationService.settings.getOverviewIntent())
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun openInBrowser(url: String) {
        WmdwCustomTabsStyle
                .createCustomTabsSessionWithWmdwStyle(this, customTabsSession.value)
                .launchUrl(this, url.toUri())
    }

    companion object {
        const val WOONWAARD_NEIGHBORHOOD_MEDIATION_URL = "https://www.woonwaard.nl/overlast"
    }
}
