package com.source.subscity.ui.cinemasmap

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.repositories.CinemaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CinemasMapPresenter @Inject constructor(private val cinemaRepository: CinemaRepository) : MvpPresenter<CinemasMapView>() {

    fun getCinemas(googleMap: Any) {
        cinemaRepository.getCinemas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showCinemas(it, googleMap) },
                        { viewState.onError(it) }
                )
    }

    fun onMarkersAdd(markerCinemaMap: Map<String, Long>) {
        viewState.onMarkersAdd(markerCinemaMap)
    }
}