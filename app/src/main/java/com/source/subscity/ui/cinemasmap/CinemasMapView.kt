package com.source.subscity.ui.cinemasmap

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemasMapView : MvpView {
    fun showCinemas(cinemas: List<Cinema>, googleMap: Any)
    fun onError(throwable: Throwable)
}