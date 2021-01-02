package nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize

/** https://github.com/saket/FluidKeyboardResize */
data class KeyboardVisibilityChanged(
    val visible: Boolean,
    val contentHeight: Int,
    val contentHeightBeforeResize: Int
)
