package nl.woonwaard.wij_maken_de_wijk.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.AuthenticationNavigationService
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.databinding.ActivityInviteScreenBinding
import org.koin.android.ext.android.inject

class InviteScreenActivity : AppCompatActivity() {
    private val authenticationNavigationService by inject<AuthenticationNavigationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityInviteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup.setOnClickListener {
            startActivity(authenticationNavigationService.getRegistrationIntent())
        }

        binding.login.setOnClickListener {
            startActivity(authenticationNavigationService.getLoginIntent())
        }
    }
}
