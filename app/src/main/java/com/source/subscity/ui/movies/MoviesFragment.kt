package com.source.subscity.ui.movies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import com.source.subscity.extensions.supportActionBar
import com.source.subscity.ui.movie.MovieActivity


/**
 * @author Vitaliy Markus
 */
class MoviesFragment : MvpAppCompatFragment(), MoviesView {

    @InjectPresenter
    lateinit var moviesPresenter: MoviesPresenter

    private lateinit var moviesList: RecyclerView

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @ProvidePresenter
    fun moviesPresenter(): MoviesPresenter {
        return SubsCityDagger.component.createMoviesPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        moviesList = inflater.inflate(R.layout.fragment_movies, container, false) as RecyclerView
        return moviesList
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_films) }
        }
    }

    override fun showMovies(movies: List<Movie>) {
        moviesList.run {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = MoviesAdapter(activity!!, movies) { MovieActivity.start(activity!!, it.id) }
        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show()
    }
}