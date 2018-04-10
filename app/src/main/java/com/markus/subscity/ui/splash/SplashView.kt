package com.markus.subscity.ui.splash

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Vitaliy Markus
 */
@StateStrategyType(SingleStateStrategy::class)
interface SplashView : MvpView {

    fun showMain()
    fun showCityPicker()
}