@file:JvmName("ViewPagerUtils")

package androidx.viewpager.widget

import android.view.View

/**
 * @author Vitaliy Markus
 */

fun ViewPager.getCurrentView(): View? {
    val currentItem = currentItem
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        val layoutParams = child.layoutParams as ViewPager.LayoutParams
        if (!layoutParams.isDecor && currentItem == layoutParams.position) {
            return child
        }
    }
    return null
}

fun ViewPager.getView(position: Int): View? {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        val layoutParams = child.layoutParams as ViewPager.LayoutParams
        if (!layoutParams.isDecor && position == layoutParams.position) {
            return child
        }
    }
    return null
}


