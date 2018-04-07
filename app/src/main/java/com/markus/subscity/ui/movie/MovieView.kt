package com.markus.subscity.ui.movie

import com.arellomobile.mvp.MvpView
import com.markus.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface MovieView : MvpView {
    fun showMovie(movie: Movie, cinemaScreenings: List<MoviePresenter.CinemaScreenings>)
    fun onError(throwable: Throwable)
}