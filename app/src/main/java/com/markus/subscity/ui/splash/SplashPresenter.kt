package com.markus.subscity.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.PreferencesProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SplashPresenter @Inject constructor(private val preferencesProvider: PreferencesProvider) : MvpPresenter<SplashView>() {

    private val LAUNCH_COUNT_MOD = 5

    fun checkFirstLaunch() {
        val preferences = preferencesProvider.getAppPreferences()
        val cityId = preferences.getString(PreferencesProvider.CITY_ID_KEY, null)
        val launchCount = preferences.getInt(PreferencesProvider.LAUNCH_COUNT, 0)
        val wasRanked = preferences.getBoolean(PreferencesProvider.WAS_RANKED, false)
        if (cityId == null) {
            viewState.showCityPicker()
//        } else if (!wasRanked && launchCount % LAUNCH_COUNT_MOD == 0) { // TODO Splash Screen
//            viewState.showOffer()
        } else {
            viewState.showMain()
        }
    }
}