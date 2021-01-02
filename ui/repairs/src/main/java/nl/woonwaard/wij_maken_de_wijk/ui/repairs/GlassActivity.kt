package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import nl.woonwaard.wij_maken_de_wijk.ui.core.customTabsSession
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityCentralHeatingBinding
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityGlassBinding

class GlassActivity : AppCompatActivity() {
    private val customTabsSession by customTabsSession()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGlassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.callButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${GLASTOTAAL_PHONE_NUMBER}")))
        }

        binding.content.moreInfoButton.setOnClickListener {
            openInBrowser(GLASTOTAAL_PROCEDURE_URL)
        }

        customTabsSession.observe(this) {
            it?.mayLaunchUrl(GLASTOTAAL_PROCEDURE_URL.toUri(), null, null)
        }
    }

    private fun openInBrowser(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(
                        CustomTabColorSchemeParams.Builder()
                                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
                                .build()
                )
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                .setUrlBarHidingEnabled(true)
                .setShowTitle(true)

        val session = customTabsSession.value
        if(session != null) customTabsBuilder.setSession(session)

        customTabsBuilder.build().launchUrl(this, url.toUri())
    }

    companion object {
        const val GLASTOTAAL_PHONE_NUMBER = "0800 4527234"
        const val GLASTOTAAL_PROCEDURE_URL = "https://www.glastotaalbeheer.nl/service/procedure-schademelding"
    }
}
