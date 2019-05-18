package com.markus.subscity.widgets

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import java.lang.ref.WeakReference

/**
 * @author Vitaliy Markus
 */
class RecyclerViewAppBarBehavior(context: Context, attrs: AttributeSet) : AppBarLayout.Behavior(context, attrs) {

    private val scrollListenerMap = HashMap<RecyclerView, RecyclerViewScrollListener>()

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        var isConsumed = consumed
        if (target is RecyclerView) {
            if (scrollListenerMap[target] == null) {
                val recyclerViewScrollListener = RecyclerViewScrollListener(coordinatorLayout, child, this)
                scrollListenerMap[target] = recyclerViewScrollListener
                target.addOnScrollListener(recyclerViewScrollListener)
            }
            scrollListenerMap[target]?.let {
                it.velocity = velocityY
                isConsumed = it.scrolledY > 0
            }
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, isConsumed)
    }

    private class RecyclerViewScrollListener(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, barBehavior: RecyclerViewAppBarBehavior) : RecyclerView.OnScrollListener() {
        var scrolledY: Int = 0
            private set

        var velocity: Float = 0f
        private var dragging: Boolean = false
        private val coordinatorLayoutRef: WeakReference<CoordinatorLayout> = WeakReference(coordinatorLayout)
        private val childRef: WeakReference<AppBarLayout> = WeakReference(child)
        private val behaviorWeakReference: WeakReference<RecyclerViewAppBarBehavior> = WeakReference(barBehavior)

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            dragging = newState == RecyclerView.SCROLL_STATE_DRAGGING
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            scrolledY += dy
            val child = childRef.get()
            val coordinatorLayout = coordinatorLayoutRef.get()
            val behavior = behaviorWeakReference.get()
            if (scrolledY <= 0 && !dragging && child != null && coordinatorLayout != null && behavior != null) {
                behavior.onNestedFling(coordinatorLayout, child, recyclerView, 0f, velocity, false)
            }
        }
    }
}