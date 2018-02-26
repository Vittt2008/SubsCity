package com.source.subscity.ui.cinemas

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
class CinemasPresenter @Inject constructor(private val cinemaRepository: CinemaRepository) : MvpPresenter<CinemasView>() {

    override fun onFirstViewAttach() {
        cinemaRepository.getCinemas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showCinemas(it) },
                        { viewState.onError(it) }
                )
    }
}