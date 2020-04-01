package com.markus.subscity.ui.movies

import com.arellomobile.mvp.InjectViewState
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.MovieRepository
import com.markus.subscity.ui.base.BaseMvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviesPresenter @Inject constructor(private val movieRepository: MovieRepository,
                                          private val cityProvider: CityProvider) : BaseMvpPresenter<MoviesView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress()
        cityProvider.asyncCity
                .flatMapSingle { movieRepository.getMovies() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTillDetach({ movies ->
                    if (movies.isEmpty()) {
                        viewState.showEmptyMessage()
                    } else {
                        viewState.showMovies(movies)
                    }
                }, {
                    viewState.onError(it)
                })
    }
}
