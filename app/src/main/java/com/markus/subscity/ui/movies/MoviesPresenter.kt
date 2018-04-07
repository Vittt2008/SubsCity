package com.markus.subscity.ui.movies

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.repositories.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviesPresenter @Inject constructor(private val movieRepository: MovieRepository,
                                          private val cityProvider: CityProvider) : MvpPresenter<MoviesView>() {

    override fun onFirstViewAttach() {
        viewState.showProgress()
        cityProvider.asyncCity
                .flatMapSingle { movieRepository.getMovies() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.hideProgress()
                    viewState.showMovies(it)
                }, {
                    viewState.hideProgress()
                    viewState.onError(it)
                })
    }
}
