package com.source.subscity.ui.cinemas

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.City
import com.source.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemasView : MvpView {
    fun showCinemas(cinemas: List<Cinema>)
    fun onError(throwable: Throwable)
    fun showCinemasMap(city: City)
}