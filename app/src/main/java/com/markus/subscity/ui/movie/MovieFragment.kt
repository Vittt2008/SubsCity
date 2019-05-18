package com.markus.subscity.ui.movie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.transition.TransitionManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
//import com.markus.subscity.dagger.GlideApp
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.*
import com.markus.subscity.ui.cinema.CinemaActivity
import com.markus.subscity.ui.share.SharePresenter
import com.markus.subscity.ui.share.ShareView
import com.markus.subscity.ui.youtube.YouTubeActivity
import com.markus.subscity.utils.FileUtils
import com.markus.subscity.utils.IntentUtils
import com.markus.subscity.widgets.ScrollableLinearLayoutManager
//import com.markus.subscity.widgets.transformations.PosterCrop
import java.io.File

/**
 * @author Vitaliy Markus
 */
class MovieFragment : MvpAppCompatFragment(), MovieView, ShareView {

    @InjectPresenter
    lateinit var moviesPresenter: MoviePresenter

    @InjectPresenter
    lateinit var sharePresenter: SharePresenter

    private lateinit var moviePoster: ImageView
    private lateinit var trailerButton: ImageView
    private lateinit var toolbarLayout: CollapsingToolbarLayout
    private lateinit var movieInfoList: androidx.recyclerview.widget.RecyclerView
    private lateinit var movieInfoListLayoutManager: ScrollableLinearLayoutManager

    private lateinit var shareMenuItem: MenuItem

    private var adapter: MovieAdapterDelegates? = null

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

    @ProvidePresenter
    fun sharePresenter(): SharePresenter {
        return SubsCityDagger.component.createSharePresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        moviePoster = root.findViewById(R.id.iv_movie_poster)
        trailerButton = root.findViewById(R.id.iv_play)
        toolbarLayout = root.findViewById(R.id.toolbar_layout)
        movieInfoList = root.findViewById(R.id.rv_list)
        movieInfoListLayoutManager = ScrollableLinearLayoutManager(activity!!)
        movieInfoList.layoutManager = movieInfoListLayoutManager
        setSupportActionBar(root.findViewById(R.id.toolbar))
        toolbarLayout.title = ""
        supportActionBar.title = ""
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        menu.findItem(R.id.item_share).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_share) {
            val movie = adapter!!.movie
            analytics().logShareMovie(movie.id, movie.title.russian)
            sharePresenter.share(moviePoster.drawable, movie)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.item_share).isVisible = adapter != null
    }

    override fun showMovie(movie: Movie, cinemaScreenings: List<MoviePresenter.CinemaScreenings>) {
        activity!!.invalidateOptionsMenu()
        if (adapter == null) {
            toolbarLayout.title = movie.title.russian
            //GlideApp.with(moviePoster).asBitmap().load(movie.poster).transform(PosterCrop()).into(moviePoster)
            adapter = MovieAdapterDelegates(movie, cinemaScreenings, ::buyTicket, ::openCinema)
            movieInfoList.adapter = adapter
            if (movie.trailer.hasTrailer) {
                showTrailerButton(movie)
            }
        } else {
            movieInfoListLayoutManager.isScrollEnabled = false
            adapter!!.updateScreenings(cinemaScreenings)
            movieInfoList.post { movieInfoListLayoutManager.isScrollEnabled = true }
        }

    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
        adapter?.updateScreenings(emptyList())
    }

    override fun openCinema(cinema: Cinema) {
        analytics().logOpenCinema(cinema.id, cinema.name, false)
        CinemaActivity.start(activity!!, cinema.id)
    }

    override fun share(file: File?, title: String, content: String) {
        val intent = createIntent(activity!!, file, getString(R.string.movie_share_title, title), content)
        openIntent(intent, R.string.movie_no_share_application)
    }

    override fun onShareError() {
        toast(getString(R.string.movie_share_error))
    }

    private fun showTrailerButton(movie: Movie) {
        TransitionManager.beginDelayedTransition(view!!.findViewById(R.id.toolbar_layout))
        trailerButton.visibility = View.VISIBLE
        trailerButton.setOnClickListener { showPreferOriginalTrailer(movie) }
    }

    private fun showPreferOriginalTrailer(movie: Movie) {
        val trailer = movie.trailer
        when {
            trailer.original.isNotEmpty() -> openYoutube(movie, trailer.original, true)
            trailer.russian.isNotEmpty() -> openYoutube(movie, trailer.russian, false)
            else -> toast(getString(R.string.movie_no_trailers))
        }
    }

    private fun openYoutube(movie: Movie, trailerId: String, isOriginal: Boolean) {
        analytics().logOpenYouTube(movie.id, movie.title.russian, trailerId, isOriginal)
        YouTubeActivity.start(activity!!, trailerId)
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
                            R.id.item_original -> openYoutube(movie, trailer.original, true)
                            R.id.item_russian -> openYoutube(movie, trailer.russian, false)
                        }
                    })
                    .createDialog()
                    .show()
        } else if (trailer.original.isNotEmpty()) {
            openYoutube(movie, trailer.original, true)
        } else if (trailer.russian.isNotEmpty()) {
            openYoutube(movie, trailer.russian, false)
        } else {
            toast(getString(R.string.movie_no_trailers))
        }
    }

    private fun buyTicket(screening: Screening) {
        analytics().logBuyTicket(screening)
        openUrl(Uri.parse(screening.ticketsUrl))
    }

    private fun createIntent(context: Context, file: File?, title: String, content: String): Intent {
        if (file != null && file.exists()) {
            val image = FileUtils.getUriForShareFile(context, file)
            return IntentUtils.createShareTextWithImageIntent(context, title, content, image)
        } else {
            return IntentUtils.createShareTextIntent(context, title, content)
        }
    }
}
