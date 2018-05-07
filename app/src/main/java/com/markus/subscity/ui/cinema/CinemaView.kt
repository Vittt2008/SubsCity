package com.markus.subscity.ui.cinema

import com.arellomobile.mvp.MvpView
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface CinemaView : MvpView {
    fun showCinema(cinema: Cinema, second: List<CinemaPresenter.MovieScreenings>)
    fun onError(throwable: Throwable)
    fun openMovie(movie:Movie)
}