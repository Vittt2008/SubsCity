package com.markus.subscity.ui.offer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.PreferencesProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class OfferPresenter @Inject constructor(private val preferencesProvider: PreferencesProvider) : MvpPresenter<OfferView>() {

    fun openPlayStore() {
        preferencesProvider.getAppPreferences().edit().putBoolean(PreferencesProvider.WAS_RANKED, true).apply()
        viewState.openPlayStore()
    }

}