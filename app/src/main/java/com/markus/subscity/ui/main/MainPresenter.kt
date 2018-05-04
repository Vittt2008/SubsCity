package com.markus.subscity.ui.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.PreferencesProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MainPresenter @Inject constructor(private val preferencesProvider: PreferencesProvider) : MvpPresenter<MainView>() {

    private val LAUNCH_COUNT_MOD = 15

    override fun onFirstViewAttach() {
        val preferences = preferencesProvider.getAppPreferences()
        val launchCount = preferences.getInt(PreferencesProvider.LAUNCH_COUNT, 0)
        val wasRanked = preferences.getBoolean(PreferencesProvider.WAS_RANKED, false)
        if (!wasRanked && launchCount % LAUNCH_COUNT_MOD == 0) {
            viewState.showRateDialog()
        }
    }

    fun openPlayStore() {
        preferencesProvider.getAppPreferences().edit().putBoolean(PreferencesProvider.WAS_RANKED, true).apply()
        viewState.openPlayStore()
    }
}