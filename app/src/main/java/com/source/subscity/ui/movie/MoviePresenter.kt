package com.source.subscity.ui.movie

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.repositories.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviePresenter @Inject constructor(private val movieRepository: MovieRepository) : MvpPresenter<MovieView>() {

    var movieId: Long = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        movieRepository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showMovie(it) },
                        { viewState.onError(it) }
                )
    }
}