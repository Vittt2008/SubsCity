package com.source.subscity.ui.cinema

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
class CinemaPresenter @Inject constructor(private val cinemaRepository: CinemaRepository) : MvpPresenter<CinemaView>() {

    var cinemaId: Long = 0

    override fun onFirstViewAttach() {
        cinemaRepository.getCinema(cinemaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showCinema(it) },
                        { viewState.onError(it) }
                )
    }
}
