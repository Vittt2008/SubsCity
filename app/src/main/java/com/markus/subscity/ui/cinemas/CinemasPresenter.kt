package com.markus.subscity.ui.cinemas

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.ui.base.BaseMvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CinemasPresenter @Inject constructor(private val cinemaRepository: CinemaRepository,
                                           private val cityProvider: CityProvider) : BaseMvpPresenter<CinemasView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress()
        cityProvider.asyncCity
                .flatMapSingle { cinemaRepository.getCinemas() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTillDetach({ cinemas ->
                    if (cinemas.isEmpty()) {
                        viewState.showEmptyMessage()
                    } else {
                        viewState.showCinemas(cinemas)
                    }
                }, {
                    viewState.onError(it)
                })
    }

    fun showCinemasMap() {
        viewState.showCinemasMap(cityProvider.city)
    }
}