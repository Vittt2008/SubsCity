package com.markus.subscity.ui.deeplink

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Vitaliy Markus
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface DeepLinkView : MvpView {
    fun showMain()
    fun showMovies()
    fun showCinemas()
    fun showMovie(movieId: Long)
    fun showCinema(cinemaId: Long)
}