package com.markus.subscity.ui.movie.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.DurationProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieInfoDelegate : AbsListItemAdapterDelegate<Movie, Any, MovieInfoDelegate.InfoViewHolder>() {

    @Inject
    lateinit var durationProvider: DurationProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return position == 0
    }

    override fun onBindViewHolder(item: Movie, viewHolder: MovieInfoDelegate.InfoViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class InfoViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val genre = view.findViewById<TextView>(R.id.tv_genre_value)
        private val languageTitle = view.findViewById<TextView>(R.id.tv_language_title)
        private val language = view.findViewById<TextView>(R.id.tv_language_value)
        private val durationTitle = view.findViewById<TextView>(R.id.tv_durability_title)
        private val duration = view.findViewById<TextView>(R.id.tv_durability_value)
        private val age = view.findViewById<TextView>(R.id.tv_age_value)
        private val ratingTitle = view.findViewById<TextView>(R.id.tv_rating_title)
        private val rating = view.findViewById<TextView>(R.id.tv_rating_value)
        private val descriptionTitle = view.findViewById<TextView>(R.id.tv_description_title)
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

            if (movie.duration == 0) {
                durationTitle.visibility = View.GONE
                duration.visibility = View.GONE
            } else {
                durationTitle.visibility = View.VISIBLE
                duration.visibility = View.VISIBLE
                duration.text = durationProvider.format(movie.duration)
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
            if (movie.description.isNotEmpty()) {
                descriptionTitle.visibility = View.VISIBLE
                description.visibility = View.VISIBLE
                description.text = movie.description
            } else {
                descriptionTitle.visibility = View.GONE
                description.visibility = View.GONE
            }

        }
    }
}