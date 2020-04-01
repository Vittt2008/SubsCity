package com.markus.subscity.ui.cinemas

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.markus.subscity.api.entities.City
import com.markus.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemasView : MvpView {

    @StateStrategyType(SingleStateStrategy::class)
    fun showCinemas(cinemas: List<Cinema>)

    @StateStrategyType(SingleStateStrategy::class)
    fun showEmptyMessage()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError(throwable: Throwable)

    fun showProgress()

    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCinemasMap(city: City)
}