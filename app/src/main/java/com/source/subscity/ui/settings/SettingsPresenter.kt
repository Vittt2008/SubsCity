package com.source.subscity.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.providers.CityProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SettingsPresenter @Inject constructor(private val cityProvider: CityProvider) : MvpPresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        viewState.showSettings(cityProvider.cityName)
    }
}