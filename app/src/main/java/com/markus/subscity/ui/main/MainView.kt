package com.markus.subscity.ui.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Vitaliy Markus
 */
@StateStrategyType(SingleStateStrategy::class)
interface MainView:MvpView{
    fun showRateDialog()
    fun openPlayStore()
}