package com.source.subscity.ui.movies

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.movie.Movie

/**
 * @author Vitaliy Markus
 */
interface MoviesView : MvpView {

    fun showMovies(movies:List<Movie>)
}