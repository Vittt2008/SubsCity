package com.source.subscity.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.providers.CityProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SettingsPresenter @Inject constructor(private val cityProvider: CityProvider) : MvpPresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        cityProvider.asyncCity
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState.showSettings(cityProvider.cityName) }

    }

    fun showCinemasMap() {
        viewState.showCinemasMap(cityProvider.city)
    }
}