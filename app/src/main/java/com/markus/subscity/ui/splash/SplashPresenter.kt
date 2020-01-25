package com.markus.subscity.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.PreferencesProvider
import com.markus.subscity.ui.base.BaseMvpPresenter
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SplashPresenter @Inject constructor(private val preferencesProvider: PreferencesProvider) : BaseMvpPresenter<SplashView>() {

    fun checkFirstLaunch() {
        val preferences = preferencesProvider.getAppPreferences()
        val cityId = preferences.getString(PreferencesProvider.CITY_ID_KEY, null)
        if (cityId == null) {
            viewState.showCityPicker()
        } else {
            viewState.showMain()
        }
    }
}