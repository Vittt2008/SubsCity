package com.source.subscity.ui.cinemas

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.source.subscity.api.entities.City
import com.source.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemasView : MvpView {
    fun showCinemas(cinemas: List<Cinema>)
    fun onError(throwable: Throwable)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCinemasMap(city: City)
}