package com.source.subscity.ui.movie

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface MovieView : MvpView {
    fun showMovie(movie: Movie, cinemaScreenings: List<MoviePresenter.CinemaScreenings>)
    fun onError(throwable: Throwable)
}