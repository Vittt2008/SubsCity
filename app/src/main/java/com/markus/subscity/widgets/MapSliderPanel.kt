package com.markus.subscity.widgets

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.widget.SliderPanel
import com.markus.subscity.R


/**
 * @author Vitaliy Markus
 */
class MapSliderPanel : SliderPanel {

    constructor(context: Context) : super(context)
    constructor(context: Context, decorView: View, config: SlidrConfig?) : super(context, decorView, config)

    private val topActiveZone = getStatusBarHeight() + getActionBarSize()

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.y > topActiveZone) {
            return false
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun getActionBarSize(): Int {
        val typedArray = context.theme.obtainStyledAttributes(intArrayOf(android.support.v7.appcompat.R.attr.actionBarSize))
        val defaultActionBarSize = resources.getDimensionPixelSize(R.dimen.default_action_bar_size)
        return typedArray.getDimensionPixelSize(0, defaultActionBarSize)
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return resources.getDimensionPixelSize(R.dimen.default_status_bar_size)
    }
}