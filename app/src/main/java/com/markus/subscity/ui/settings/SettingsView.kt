package com.markus.subscity.ui.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.markus.subscity.api.entities.City
import com.markus.subscity.providers.ThemeProvider

/**
 * @author Vitaliy Markus
 */
interface SettingsView : MvpView {

    companion object {
        const val SOON_AT_BOX_OFFICE = 0
        const val CINEMA_MAP = 1
        const val SALES = 2
        const val ABOUT = 3
        const val CITY = 4
        const val DONATE = 5
        const val RATE_APP = 6
        const val POLICY = 7
        const val THEME = 8
        const val DARK_THEME = 9
        const val DIALOG_THEME = 10
        const val LANGUAGE = 11
    }

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSettings(settings: List<Setting>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCinemasMap(city: City)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openPlayStore()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun recreate()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showThemeDialog(list: List<ThemeProvider.SelectedThemeItem>)
}