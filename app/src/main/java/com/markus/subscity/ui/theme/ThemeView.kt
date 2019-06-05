package com.markus.subscity.ui.theme

import com.arellomobile.mvp.MvpView
import com.markus.subscity.providers.ThemeProvider

/**
 * @author Vitaliy Markus
 */
interface ThemeView : MvpView {
    fun showThemes(list: List<ThemeProvider.SelectedThemeItem>)
}