package com.markus.subscity.ui.theme

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.markus.subscity.providers.ThemeProvider

/**
 * @author Vitaliy Markus
 */
interface ThemeView : MvpView {

    fun showThemes(list: List<ThemeProvider.SelectedThemeItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun recreate()
}