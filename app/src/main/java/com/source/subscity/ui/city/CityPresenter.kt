package com.source.subscity.ui.city

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.providers.CityProvider
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
                .subscribe(
                        { viewState.showCities(it) },
                        { viewState.onError(it) }
                )
    }
}
