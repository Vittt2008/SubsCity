package com.markus.subscity.ui.movies

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.controllers.ContentLoadingController
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.supportActionBar
import com.markus.subscity.ui.movie.MovieActivity


/**
 * @author Vitaliy Markus
 */
class MoviesFragment : MvpAppCompatFragment(), MoviesView {

    private lateinit var loadingController: ContentLoadingController

    @InjectPresenter
    lateinit var moviesPresenter: MoviesPresenter

    private lateinit var moviesList: androidx.recyclerview.widget.RecyclerView

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @ProvidePresenter
    fun moviesPresenter(): MoviesPresenter {
        return SubsCityDagger.component.createMoviesPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)
        moviesList = root.findViewById(R.id.rv_list)
        loadingController = ContentLoadingController(root, R.id.rv_list, R.id.pb_progress)
        return root
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            activity?.let { it.supportActionBar.setTitle(R.string.main_films) }
        }
    }

    override fun showMovies(movies: List<Movie>) {
        loadingController.switchState(ContentLoadingController.State.CONTENT)
        moviesList.run {
            layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
            adapter = MoviesAdapter(activity!!, movies, ::openMovie)
        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        loadingController.switchState(ContentLoadingController.State.PROGRESS)
    }

    override fun hideProgress() {
        loadingController.switchState(ContentLoadingController.State.CONTENT)
    }

    private fun openMovie(movie: Movie) {
        analytics().logOpenMovie(movie.id, movie.title.russian, false)
        MovieActivity.start(activity!!, movie.id)
    }
}