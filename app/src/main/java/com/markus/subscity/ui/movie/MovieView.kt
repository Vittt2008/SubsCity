package com.markus.subscity.ui.movie

import com.arellomobile.mvp.MvpView
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface MovieView : MvpView {
    fun showTitle(title: String)
    fun showMovie(movie: Movie, cinemaScreenings: List<MoviePresenter.CinemaScreenings>)
    fun onError(throwable: Throwable)
    fun openCinema(cinema: Cinema)
}