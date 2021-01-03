package nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.enableFluidContentResizer() {
    FluidContentResizer.listen(this)
}
