package com.markus.subscity.ui.cinemasmap

import com.arellomobile.mvp.MvpView
import com.markus.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemasMapView : MvpView {
    fun showCinemas(cinemas: List<Cinema>, googleMap: Any)
    fun onError(throwable: Throwable)
    fun onMarkersAdd(markerCinemaMap: Map<String, Long>, markers: List<Any>)
}