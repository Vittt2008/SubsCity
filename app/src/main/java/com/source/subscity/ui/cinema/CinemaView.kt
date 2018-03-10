package com.source.subscity.ui.cinema

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.cinema.Cinema

/**
 * @author Vitaliy Markus
 */
interface CinemaView : MvpView {
    fun showCinema(cinema: Cinema)
    fun onError(throwable: Throwable)
}