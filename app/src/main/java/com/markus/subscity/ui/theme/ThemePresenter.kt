package com.markus.subscity.ui.theme

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.ThemeProvider
import com.markus.subscity.ui.base.BaseMvpPresenter
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class ThemePresenter @Inject constructor(private val themeProvider: ThemeProvider) : BaseMvpPresenter<ThemeView>() {

    override fun onFirstViewAttach() {
        val themeList = themeProvider.createThemeList()
        viewState.showThemes(themeList)
    }

    fun updateTheme(item: ThemeProvider.SelectedThemeItem) {
        themeProvider.applyTheme(item.mode)
                .subscribeTillDetach { recreate ->
                    if (recreate) {
                        viewState.recreate()
                    }
                }
    }
}