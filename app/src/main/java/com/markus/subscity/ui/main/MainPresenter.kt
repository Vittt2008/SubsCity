package com.markus.subscity.ui.main

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.PreferencesProvider
import com.markus.subscity.ui.base.BaseMvpPresenter
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MainPresenter @Inject constructor(private val preferencesProvider: PreferencesProvider) : BaseMvpPresenter<MainView>() {

    companion object {
        private const val LAUNCH_COUNT_MOD = 15
    }

    override fun onFirstViewAttach() {
        val preferences = preferencesProvider.getAppPreferences()
        val launchCount = preferences.getInt(PreferencesProvider.LAUNCH_COUNT_KEY, 0)
        val wasRanked = preferences.getBoolean(PreferencesProvider.WAS_RANKED_KEY, false)
        if (!wasRanked && launchCount % LAUNCH_COUNT_MOD == 0) {
            viewState.showRateDialog()
        }
    }

    fun openPlayStore() {
        preferencesProvider.getAppPreferences().edit().putBoolean(PreferencesProvider.WAS_RANKED_KEY, true).apply()
        viewState.openPlayStore()
    }
}