package com.markus.subscity.controllers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import androidx.annotation.IdRes
import kotlin.math.max

/**
 * @author Vitaliy Markus
 */
class ContentLoadingController {

    private val contentViews: Array<View>
    private val progressView: View
    private var emptyView: View? = null

    private val handler: Handler
    private var currentState: State? = null
    private var minProgressShowTime: Int
    private var progressShowDelay: Int
    private var lastStateSwitchTimestamp: Long = 0
    private var switchStateCallback: Runnable? = null

    private var animated = false

    companion object {
        private const val MIN_PROGRESS_SHOW_TIME = 500
        private const val PROGRESS_SHOW_DELAY = 500
        private const val CROSS_FADE_DURATION = 300L
    }

    enum class State {
        CONTENT,
        EMPTY,
        PROGRESS
    }


    constructor(rootView: View, contentViewIds: IntArray, @IdRes progressViewId: Int, @IdRes emptyViewId: Int = View.NO_ID) {
        this.contentViews = contentViewIds.map { viewId ->
            rootView.findViewById<View>(viewId) ?: throw IllegalArgumentException("View with id $viewId was not found")
        }.toTypedArray()
        this.progressView = rootView.findViewById(progressViewId) ?: throw IllegalArgumentException("View with id $progressViewId was not found")
        this.handler = Handler(Looper.getMainLooper())
        this.minProgressShowTime = MIN_PROGRESS_SHOW_TIME
        this.progressShowDelay = PROGRESS_SHOW_DELAY
        if (emptyViewId != View.NO_ID) {
            emptyView = rootView.findViewById(emptyViewId) ?: throw IllegalArgumentException("View with id $emptyViewId was not found")
        }
        setContentState(State.CONTENT)
    }

    constructor(activity: Activity, contentViewIds: IntArray, @IdRes progressViewId: Int, @IdRes emptyViewId: Int = View.NO_ID) :
            this(activity.window.decorView, contentViewIds, progressViewId, emptyViewId)

    constructor(rootView: View, @IdRes contentViewId: Int, @IdRes progressViewId: Int, @IdRes emptyViewId: Int = View.NO_ID) :
            this(rootView, intArrayOf(contentViewId), progressViewId, emptyViewId)

    constructor(activity: Activity, @IdRes contentViewId: Int, @IdRes progressViewId: Int, @IdRes emptyViewId: Int = View.NO_ID) :
            this(activity.window.decorView, contentViewId, progressViewId, emptyViewId)


    fun setAnimated(animated: Boolean): ContentLoadingController {
        this.animated = animated
        return this
    }

    fun setMinProgressShowTime(minProgressShowTime: Int): ContentLoadingController {
        this.minProgressShowTime = minProgressShowTime
        return this
    }

    fun setProgressShowDelay(progressShowDelay: Int): ContentLoadingController {
        this.progressShowDelay = progressShowDelay
        return this
    }

    fun switchState(state: State) {
        switchStateCallback?.let(handler::removeCallbacks)

        if (currentState == state) {
            return
        }

        val switchStateCallback = Runnable { setContentState(state) }
        this.switchStateCallback = switchStateCallback
        val elapsedTime = SystemClock.elapsedRealtime() - lastStateSwitchTimestamp
        when (state) {
            State.CONTENT, State.EMPTY -> handler.postDelayed(switchStateCallback, max(0, minProgressShowTime - elapsedTime))
            State.PROGRESS -> handler.postDelayed(switchStateCallback, progressShowDelay.toLong())
        }
    }

    fun setContentState(state: State) {
        currentState = state
        when (state) {
            State.CONTENT -> {
                hideView(progressView)
                showView(contentViews)
                hideView(emptyView)
            }
            State.EMPTY -> {
                hideView(progressView)
                hideView(contentViews)
                showView(emptyView)
            }
            State.PROGRESS -> {
                showView(progressView)
                hideView(contentViews)
                hideView(emptyView)
            }
        }
        lastStateSwitchTimestamp = SystemClock.elapsedRealtime()
    }

    private fun showView(view: View?) {
        if (view == null) {
            return
        }
        if (view.visibility != View.VISIBLE) {
            if (animated) {
                view.visibility = View.VISIBLE
                view.alpha = 0f
                view.animate()
                        .alpha(1f)
                        .setDuration(CROSS_FADE_DURATION)
                        .setListener(null)
                        .start()
            } else {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun showView(views: Array<View>) {
        views.forEach { view -> showView(view) }
    }

    private fun hideView(view: View?) {
        if (view == null) {
            return
        }
        if (view.visibility == View.VISIBLE) {
            if (animated) {
                view.animate()
                        .alpha(0f)
                        .setDuration(CROSS_FADE_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                view.visibility = View.GONE
                            }
                        })
                        .start()
            } else {
                view.visibility = View.GONE
            }
        }
    }

    private fun hideView(views: Array<View>) {
        views.forEach { view -> hideView(view) }
    }
}