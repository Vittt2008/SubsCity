package com.source.subscity.ui.cinema

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.LanguageProvider
import com.source.subscity.providers.MetroProvider
import com.source.subscity.ui.movie.MovieAdapter
import com.source.subscity.ui.movie.MoviePresenter
import com.source.subscity.ui.movie.MovieScreeningAdapter
import com.source.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaAdapter(private val cinema: Cinema,
                    private var movieScreenings: List<CinemaPresenter.MovieScreenings>,
                    private val mapClickListener: (Cinema) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CINEMA_INFO_VIEW_TYPE = 0
    private val CINEMA_MOVIES_TITLE_VIEW_TYPE = 1
    private val CINEMA_SCREENING_VIEW_TYPE = 2

    private val SPAN_COUNT = 5

    @Inject
    lateinit var metroProvider: MetroProvider

    @Inject
    lateinit var languageProvider: LanguageProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CINEMA_INFO_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_info, parent, false)
                InfoViewHolder(view)
            }
            CINEMA_MOVIES_TITLE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
                TitleDividerViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
                MovieScreeningsViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2 + movieScreenings.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            position == CINEMA_INFO_VIEW_TYPE -> (holder as InfoViewHolder).bind(cinema)
            position != CINEMA_MOVIES_TITLE_VIEW_TYPE -> (holder as MovieScreeningsViewHolder).bind(movieScreenings[position - 2])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            CINEMA_INFO_VIEW_TYPE -> CINEMA_INFO_VIEW_TYPE
            CINEMA_MOVIES_TITLE_VIEW_TYPE -> CINEMA_MOVIES_TITLE_VIEW_TYPE
            else -> CINEMA_SCREENING_VIEW_TYPE
        }
    }

    fun updateScreenings(movieScreenings: List<CinemaPresenter.MovieScreenings>) {
        val oldSize = itemCount
        this.movieScreenings = movieScreenings
        notifyItemRangeInserted(oldSize, itemCount - oldSize)
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val mapButton = view.findViewById<ViewGroup>(R.id.cl_cinema_map)

        init {
            mapButton.setOnClickListener { mapClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
        }
    }

    inner class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<TextView>(R.id.tv_divider_title)
                    .setText(R.string.movie_screenings_title)
        }
    }

    inner class MovieScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieTitle = view.findViewById<TextView>(R.id.tv_title)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        fun bind(movieScreenings: CinemaPresenter.MovieScreenings) {
            movieTitle.text = movieScreenings.movie.title.russian
            movieLanguage.text = movieLanguage(movieScreenings.movie)
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context!!, movieScreenings.screenings)
                val margin = context.resources.getDimensionPixelSize(R.dimen.screening_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, margin, false))
            }
        }
    }

    private fun movieLanguage(movie: Movie): String {
        val movieLanguage = languageProvider.languageFormat(movie)
        val age = movie.ageRestriction.toString() + "+"
        if (movieLanguage.isNullOrEmpty()) {
            return age
        }
        return "$movieLanguage, $age"
    }
}