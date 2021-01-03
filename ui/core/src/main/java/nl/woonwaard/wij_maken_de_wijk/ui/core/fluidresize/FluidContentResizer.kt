package nl.woonwaard.wij_maken_de_wijk.ui.core.fluidresize

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/** https://github.com/saket/FluidKeyboardResize */
object FluidContentResizer {
    private var heightAnimator: ValueAnimator = ObjectAnimator()

    fun listen(activity: AppCompatActivity) {
        val viewHolder = ActivityViewHolder.createFrom(activity)

        KeyboardVisibilityDetector.listen(viewHolder) {
            animateHeight(viewHolder, it)
        }
        viewHolder.onDetach {
            heightAnimator.cancel()
        }
    }

    private fun animateHeight(viewHolder: ActivityViewHolder, event: KeyboardVisibilityChanged) {
        val contentView = viewHolder.contentView
        contentView.setHeight(event.contentHeightBeforeResize)

        heightAnimator.cancel()

        // Warning: animating height might not be very performant. Try turning on
        // "Profile GPI rendering" in developer options and check if the bars stay
        // under 16ms. Using Transitions API would be more efficient, but for some
        // reason it skips the first animation and I cannot figure out why.
        heightAnimator =
            ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight).apply {
                interpolator = FastOutSlowInInterpolator()
                duration = 300
            }
        heightAnimator.addUpdateListener { contentView.setHeight(it.animatedValue as Int) }
        heightAnimator.start()
    }

    private fun View.setHeight(height: Int) {
        val params = layoutParams
        params.height = height
        layoutParams = params
    }
}
