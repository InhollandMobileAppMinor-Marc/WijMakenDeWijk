package nl.woonwaard.wij_maken_de_wijk.ui.repairs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.databinding.ActivityCentralHeatingBinding

class CentralHeatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCentralHeatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.callButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${HVC_PHONE_NUMBER}")))
        }
    }

    companion object {
        const val HVC_PHONE_NUMBER = "0800 4823637"
    }
}
