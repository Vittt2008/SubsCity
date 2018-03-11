package com.source.subscity.ui.movie

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.DurationProvider
import com.source.subscity.providers.MetroProvider
import com.source.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieAdapter(private val movie: Movie,
                   private var cinemaScreenings: List<MoviePresenter.CinemaScreenings>,
                   private val screeningClickListener: (Screening)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MOVIE_INFO_VIEW_TYPE = 0
    private val MOVIE_SCREENINGS_TITLE_VIEW_TYPE = 1
    private val MOVIE_SCREENINGS_VIEW_TYPE = 2

    private val SPAN_COUNT = 5

    @Inject
    lateinit var durationProvider: DurationProvider

    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_INFO_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_info, parent, false)
                InfoViewHolder(view)
            }
            MOVIE_SCREENINGS_TITLE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
                TitleDividerViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
                CinemaScreeningsViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2 + cinemaScreenings.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            position == MOVIE_INFO_VIEW_TYPE -> (holder as InfoViewHolder).bind(movie)
            position != MOVIE_SCREENINGS_TITLE_VIEW_TYPE -> (holder as CinemaScreeningsViewHolder).bind(cinemaScreenings[position - 2])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            MOVIE_INFO_VIEW_TYPE -> MOVIE_INFO_VIEW_TYPE
            MOVIE_SCREENINGS_TITLE_VIEW_TYPE -> MOVIE_SCREENINGS_TITLE_VIEW_TYPE
            else -> MOVIE_SCREENINGS_VIEW_TYPE
        }
    }

    fun updateScreenings(cinemaScreenings: List<MoviePresenter.CinemaScreenings>) {
        val oldSize = itemCount
        this.cinemaScreenings = cinemaScreenings
        notifyItemRangeInserted(oldSize, itemCount - oldSize)
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val genre = view.findViewById<TextView>(R.id.tv_genre_value)
        private val languageTitle = view.findViewById<TextView>(R.id.tv_language_title)
        private val language = view.findViewById<TextView>(R.id.tv_language_value)
        private val duration = view.findViewById<TextView>(R.id.tv_durability_value)
        private val age = view.findViewById<TextView>(R.id.tv_age_value)
        private val ratingTitle = view.findViewById<TextView>(R.id.tv_rating_title)
        private val rating = view.findViewById<TextView>(R.id.tv_rating_value)
        private val description = view.findViewById<TextView>(R.id.tv_description_value)

        fun bind(movie: Movie) {
            genre.text = movie.genres.joinToString(", ").capitalize()
            val languageValue = movie.languages.joinToString(", ").capitalize()
            if (languageValue.isEmpty()) {
                languageTitle.visibility = View.GONE
                language.visibility = View.GONE
            } else {
                languageTitle.visibility = View.VISIBLE
                language.visibility = View.VISIBLE
                language.text = languageValue
            }

            duration.text = durationProvider.format(movie.duration)
            age.text = "${movie.ageRestriction}+"
            val ratingValue = movie.rating
            if (ratingValue.kinopoisk.rating != 0.0) {
                ratingTitle.visibility = View.VISIBLE
                ratingTitle.setText(R.string.movie_rating_kinopoisk_title)
                rating.visibility = View.VISIBLE
                rating.text = String.format("%.${2}f", ratingValue.kinopoisk.rating)
            } else if (ratingValue.imdb.rating != 0.0) {
                ratingTitle.visibility = View.VISIBLE
                ratingTitle.setText(R.string.movie_rating_imdb_title)
                rating.visibility = View.VISIBLE
                rating.text = String.format("%.${2}f", ratingValue.imdb.rating)
            } else {
                ratingTitle.visibility = View.GONE
                rating.visibility = View.GONE
            }
            description.text = movie.description
        }
    }

    inner class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<TextView>(R.id.tv_divider_title)
                    .setText(R.string.movie_screenings_title)
        }
    }

    inner class CinemaScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cinemaTitle = view.findViewById<TextView>(R.id.tv_title)
        private val cinemaMetro = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        fun bind(cinemaScreenings: MoviePresenter.CinemaScreenings) {
            cinemaTitle.text = cinemaScreenings.cinema.name
            cinemaMetro.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinemaScreenings.cinema.location.metro)
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context!!, cinemaScreenings.screenings, screeningClickListener)
                val margin = context.resources.getDimensionPixelSize(R.dimen.screening_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, margin, false))
            }
        }
    }
}