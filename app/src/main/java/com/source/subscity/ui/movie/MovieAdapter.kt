package com.source.subscity.ui.movie

import android.content.Context
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
class MovieAdapter(context: Context, private val movie: Movie) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_info, parent, false)
                InfoViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            MOVIE_INFO_VIEW_TYPE -> (holder as InfoViewHolder).bind(movie)
            MOVIE_DATE_VIEW_TYPE -> (holder as SessionsViewHolder).bind(movie)
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            MOVIE_INFO_VIEW_TYPE -> MOVIE_INFO_VIEW_TYPE
            MOVIE_DATE_VIEW_TYPE -> MOVIE_DATE_VIEW_TYPE
            else -> MOVIE_INFO_VIEW_TYPE
        }
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
}