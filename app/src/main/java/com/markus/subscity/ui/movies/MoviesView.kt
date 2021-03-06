package com.markus.subscity.ui.movies

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.markus.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface MoviesView : MvpView {

    @StateStrategyType(SingleStateStrategy::class)
    fun showMovies(movies: List<Movie>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError(throwable: Throwable)

    fun showProgress()

    fun hideProgress()
}