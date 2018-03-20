package com.source.subscity.ui.movie

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.source.subscity.R
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.dagger.GlideApp
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.openUrl
import com.source.subscity.extensions.setSupportActionBar
import com.source.subscity.extensions.toast
import com.source.subscity.ui.youtube.YouTubeActivity
import com.source.subscity.widgets.ScrollableLinearLayoutManager
import com.source.subscity.widgets.transformations.PosterCrop

/**
 * @author Vitaliy Markus
 */
class MovieFragment : MvpAppCompatFragment(), MovieView {

    @InjectPresenter
    lateinit var moviesPresenter: MoviePresenter

    private lateinit var moviePoster: ImageView
    private lateinit var trailerButton: ImageView
    private lateinit var toolbarLayout: CollapsingToolbarLayout
    private lateinit var movieInfoList: RecyclerView
    private lateinit var movieInfoListLayoutManager: ScrollableLinearLayoutManager

    private var adapter: MovieAdapter? = null

    companion object {
        fun newInstance(movieId: Long): MovieFragment {
            return MovieFragment().apply {
                arguments = Bundle().apply {
                    putLong(MovieActivity.EXTRA_MOVIE_ID, movieId)
                }
            }
        }
    }

    @ProvidePresenter
    fun moviesPresenter(): MoviePresenter {
        return SubsCityDagger.component.createMoviePresenter().apply {
            movieId = arguments!!.getLong(MovieActivity.EXTRA_MOVIE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        moviePoster = root.findViewById(R.id.iv_movie_poster)
        trailerButton = root.findViewById(R.id.iv_play)
        toolbarLayout = root.findViewById(R.id.toolbar_layout)
        movieInfoList = root.findViewById(R.id.rv_list)
        movieInfoListLayoutManager = ScrollableLinearLayoutManager(activity!!)
        movieInfoList.layoutManager = movieInfoListLayoutManager
        activity!!.setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showMovie(movie: Movie, cinemaScreenings: List<MoviePresenter.CinemaScreenings>) {
        if (adapter == null) {
            toolbarLayout.title = movie.title.russian
            GlideApp.with(moviePoster).asBitmap().load(movie.poster).transform(PosterCrop()).into(moviePoster)
            adapter = MovieAdapter(movie, cinemaScreenings, ::buyTicket)
            movieInfoList.adapter = adapter
            trailerButton.setOnClickListener { showTrailer(movie) }
        } else {
            movieInfoListLayoutManager.isScrollEnabled = false
            adapter!!.updateScreenings(cinemaScreenings)
            movieInfoList.post { movieInfoListLayoutManager.isScrollEnabled = true }
        }

    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    private fun showTrailer(movie: Movie) {
        val trailer = movie.trailer
        if (trailer.original.isNotEmpty() && trailer.russian.isNotEmpty()) {
            BottomSheetBuilder(activity)
                    .setMode(BottomSheetBuilder.MODE_LIST)
                    .setMenu(R.menu.menu_trailer)
                    .setItemTextColorResource(R.color.subtitle_color)
                    .setItemClickListener({ item ->
                        when (item.itemId) {
                            R.id.item_original -> YouTubeActivity.start(activity!!, trailer.original)
                            R.id.item_russian -> YouTubeActivity.start(activity!!, trailer.russian)
                        }
                    })
                    .createDialog()
                    .show()
        } else if (trailer.original.isNotEmpty()) {
            YouTubeActivity.start(activity!!, trailer.original)
        } else if (trailer.russian.isNotEmpty()) {
            YouTubeActivity.start(activity!!, trailer.russian)
        } else {
            toast(getString(R.string.movie_no_trailers))
        }
    }

    private fun buyTicket(screening: Screening) {
        openUrl(Uri.parse(screening.ticketsUrl))
    }
}
