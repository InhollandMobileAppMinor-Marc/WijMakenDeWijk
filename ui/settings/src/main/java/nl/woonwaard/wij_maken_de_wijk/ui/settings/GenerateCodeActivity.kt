package nl.woonwaard.wij_maken_de_wijk.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.woonwaard.wij_maken_de_wijk.domain.models.Location
import nl.woonwaard.wij_maken_de_wijk.domain.models.RegistrationCodes
import nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize.enableFluidContentResizer
import nl.woonwaard.wij_maken_de_wijk.ui.core.hideKeyboard
import nl.woonwaard.wij_maken_de_wijk.ui.settings.databinding.ActivityGenerateCodeBinding
import java.util.*

class GenerateCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGenerateCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableFluidContentResizer()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.content.createInviteCodeButton.setOnClickListener {
            hideKeyboard()

            val location = when(binding.content.radioGroup.checkedRadioButtonId) {
                binding.content.testLocationRadioButton.id -> Location.TEST_LOCATION
                binding.content.vanDeVeldelaanRadioButton.id -> Location.VAN_DE_VELDELAAN
                binding.content.dillenburgstraatRadioButton.id -> Location.DILLENBURGSTRAAT
                else -> Location.TEST_LOCATION
            }

            val hallway = binding.content.hallwayInputField.text.toString()
            val houseNumber = binding.content.houseNumberInputField.text.toString()

            val code = RegistrationCodes.generateCode(
                location,
                hallway.toUpperCase(Locale.getDefault()),
                houseNumber.toUpperCase(Locale.getDefault())
            )

            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.code)
                .setMessage(code)
                .setNeutralButton(R.string.copy) { dialog, _ ->
                    getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText(code, code))
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
