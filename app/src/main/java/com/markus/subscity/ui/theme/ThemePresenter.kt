package com.markus.subscity.ui.theme

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.ThemeProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class ThemePresenter @Inject constructor(private val themeProvider: ThemeProvider) : MvpPresenter<ThemeView>() {

    override fun onFirstViewAttach() {
        val themeList = themeProvider.createThemeList()
        viewState.showThemes(themeList)
    }

    fun updateTheme(item: ThemeProvider.SelectedThemeItem) {
        themeProvider.applyTheme(item)
    }
}