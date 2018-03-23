package com.source.subscity.ui.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.source.subscity.api.entities.City

/**
 * @author Vitaliy Markus
 */
interface SettingsView : MvpView {
    fun showSettings(city: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCinemasMap(city: City)
}