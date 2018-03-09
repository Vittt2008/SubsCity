package com.source.subscity.ui.movie

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.providers.DurationProvider

/**
 * @author Vitaliy Markus
 */
class MovieAdapter(context: Context,
                   private val movie: Movie,
                   private var cinemaScreenings: List<MoviePresenter.CinemaScreenings>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MOVIE_INFO_VIEW_TYPE = 0
    private val MOVIE_DATE_VIEW_TYPE = 1
    private val MOVIE_SESSION_VIEW_TYPE = 2

    private val durationProvider = DurationProvider(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_INFO_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_info, parent, false)
                InfoViewHolder(view)
            }
            MOVIE_DATE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_date_picker, parent, false)
                SessionsViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_screening, parent, false)
                CinemaSessionViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2 + cinemaScreenings.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            MOVIE_INFO_VIEW_TYPE -> (holder as InfoViewHolder).bind(movie)
            MOVIE_DATE_VIEW_TYPE -> (holder as SessionsViewHolder).bind(movie)
            else -> (holder as CinemaSessionViewHolder).bind(cinemaScreenings[position - 2])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            MOVIE_INFO_VIEW_TYPE -> MOVIE_INFO_VIEW_TYPE
            MOVIE_DATE_VIEW_TYPE -> MOVIE_DATE_VIEW_TYPE
            else -> MOVIE_SESSION_VIEW_TYPE
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

    inner class SessionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.findViewById<TextView>(R.id.tv_date_sessions_value)

        fun bind(movie: Movie) {
            date.text = movie.screenings.next.toString("EEEE, d MMMM").capitalize()
        }
    }

    inner class CinemaSessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cinemaTitle = view.findViewById<TextView>(R.id.tv_cinema_title)
        private val cinemaMetro = view.findViewById<TextView>(R.id.tv_cinema_metro)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        fun bind(cinemaScreenings: MoviePresenter.CinemaScreenings) {
            cinemaTitle.text = cinemaScreenings.cinema.name
            screenings.apply {
                layoutManager = LinearLayoutManager(screenings.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = MovieScreeningAdapter(cinemaScreenings.screenings)
            }
        }
    }
}