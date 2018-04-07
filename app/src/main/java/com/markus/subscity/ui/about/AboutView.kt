package com.markus.subscity.ui.about

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Vitaliy Markus
 */
interface AboutView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openSocialNetwork(url: String)
}