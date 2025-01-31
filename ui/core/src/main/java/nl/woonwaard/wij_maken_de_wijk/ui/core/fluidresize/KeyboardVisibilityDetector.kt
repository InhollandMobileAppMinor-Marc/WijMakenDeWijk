package nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize

import android.view.ViewTreeObserver

/** https://github.com/saket/FluidKeyboardResize */
object KeyboardVisibilityDetector {
    fun listen(viewHolder: ActivityViewHolder, listener: (KeyboardVisibilityChanged) -> Unit) {
        val detector = Detector(viewHolder, listener)
        viewHolder.nonResizableLayout.viewTreeObserver.addOnPreDrawListener(detector)
        viewHolder.onDetach {
            viewHolder.nonResizableLayout.viewTreeObserver.removeOnPreDrawListener(detector)
        }
    }

    private class Detector(
        val viewHolder: ActivityViewHolder,
        val listener: (KeyboardVisibilityChanged) -> Unit
    ) : ViewTreeObserver.OnPreDrawListener {
        private var previousHeight: Int = -1

        override fun onPreDraw(): Boolean {
            // The layout flickers for a moment, usually on the first
            // animation. Intercepting this pre-draw seems to solve the problem.
            return detect().not()
        }

        private fun detect(): Boolean {
            val contentHeight = viewHolder.resizableLayout.height
            if (contentHeight == previousHeight) return false

            if (previousHeight != -1) {
                val statusBarHeight = viewHolder.resizableLayout.top
                val isKeyboardVisible =
                    contentHeight < viewHolder.nonResizableLayout.height - statusBarHeight

                listener(
                    KeyboardVisibilityChanged(
                        visible = isKeyboardVisible,
                        contentHeight = contentHeight,
                        contentHeightBeforeResize = previousHeight
                    )
                )
            }

            previousHeight = contentHeight
            return true
        }
    }
}

