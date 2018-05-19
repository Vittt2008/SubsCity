package com.markus.subscity.utils

import android.animation.ArgbEvaluator
import android.app.Activity
import android.os.Build
import android.support.annotation.ColorInt
import android.view.ViewGroup
import com.markus.subscity.widgets.MapSliderPanel
import com.r0adkll.slidr.R
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.widget.SliderPanel

/**
 * @author Vitaliy Markus
 */
object MapSlidr {

    fun attach(activity: Activity): SlidrInterface {
        return attach(activity, -1, -1)
    }

    fun attach(activity: Activity, @ColorInt statusBarColor1: Int, @ColorInt statusBarColor2: Int): SlidrInterface {
        val panel = attachSliderPanel(activity, null)
        panel.setOnPanelSlideListener(ColorPanelSlideListener(activity, statusBarColor1, statusBarColor2))
        return panel.defaultInterface
    }

    private fun attachSliderPanel(activity: Activity, config: SlidrConfig?): SliderPanel {
        // Hijack the decorview
        val decorView = activity.window.decorView as ViewGroup
        val oldScreen = decorView.getChildAt(0)
        decorView.removeViewAt(0)

        // Setup the slider panel and attach it to the decor
        val panel = MapSliderPanel(activity, oldScreen, config)
        panel.id = R.id.slidable_panel
        oldScreen.id = R.id.slidable_content
        panel.addView(oldScreen)
        decorView.addView(panel, 0)
        return panel
    }

    internal open class ColorPanelSlideListener(private val activity: Activity,
                                                @ColorInt protected open val primaryColor: Int,
                                                @ColorInt protected open val secondaryColor: Int) : SliderPanel.OnPanelSlideListener {

        private val evaluator = ArgbEvaluator()

        override fun onStateChanged(state: Int) {}

        override fun onClosed() {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }

        override fun onOpened() {}

        override fun onSlideChange(percent: Float) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && areColorsValid()) {
                val newColor = evaluator.evaluate(percent, primaryColor, secondaryColor) as Int
                activity.window.statusBarColor = newColor
            }
        }

        protected fun areColorsValid() = primaryColor != -1 && secondaryColor != -1
    }
}