package com.markus.subscity.ui.cinemas

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.CinemaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CinemasPresenter @Inject constructor(private val cinemaRepository: CinemaRepository,
                                           private val cityProvider: CityProvider) : MvpPresenter<CinemasView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress()
        cityProvider.asyncCity
                .flatMapSingle { cinemaRepository.getCinemas() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.hideProgress()
                    viewState.showCinemas(it)
                }, {
                    viewState.hideProgress()
                    viewState.onError(it)
                })
    }

    fun showCinemasMap() {
        viewState.showCinemasMap(cityProvider.city)
    }
}