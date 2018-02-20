package com.source.subscity.ui.movies

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.dagger.SubsCityDagger
import com.arellomobile.mvp.presenter.InjectPresenter
import com.source.subscity.api.entities.movie.Movie


/**
 * @author Vitaliy Markus
 */
class MoviesFragment : MvpAppCompatFragment(), MoviesView {

    @InjectPresenter
    lateinit var moviesPresenter: MoviesPresenter

    private lateinit var moviesList: RecyclerView

    @ProvidePresenter
    fun moviesPresenter(): MoviesPresenter {
        return SubsCityDagger.component.createMoviesPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        moviesList = inflater.inflate(R.layout.fragment_movies, container, false) as RecyclerView
        return moviesList
    }

    override fun showMovies(movies: List<Movie>) {
        moviesList.run {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = MoviesAdapter(movies)
        }
    }
}