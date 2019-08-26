package com.markus.subscity.ui.city

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.CityProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CityPresenter @Inject constructor(private val cityProvider: CityProvider) : MvpPresenter<CityView>() {

    override fun onFirstViewAttach() {
        cityProvider.supportedCities
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cities ->
                    viewState.showCities(cities, cityProvider.cityId)
                }, {
                    viewState.onError(it)
                })
    }

    fun updateCity(city: String) {
        cityProvider.changeCity(city)
    }
}
