package nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize

import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

/** https://github.com/saket/FluidKeyboardResize */
data class ActivityViewHolder(
    val nonResizableLayout: ViewGroup,
    val resizableLayout: ViewGroup,
    val contentView: ViewGroup
) {
    companion object {
        /**
         * The Activity View tree usually looks like this:
         *
         * DecorView <- does not get resized, contains space for system Ui bars.
         * - LinearLayout
         * -- FrameLayout <- gets resized
         * --- LinearLayout
         * ---- Activity content
         */
        fun createFrom(activity: AppCompatActivity): ActivityViewHolder {
            val decorView = activity.window.decorView as ViewGroup
            val contentView = decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val actionBarRootLayout = contentView.parent as ViewGroup
            val resizableLayout = actionBarRootLayout.parent as ViewGroup

            return ActivityViewHolder(
                nonResizableLayout = decorView,
                resizableLayout = resizableLayout,
                contentView = contentView
            )
        }
    }

    fun onDetach(onDetach: () -> Unit) {
        nonResizableLayout.addOnAttachStateChangeListener(object :
            View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                onDetach()
            }

            override fun onViewAttachedToWindow(v: View?) {}
        })
    }
}
