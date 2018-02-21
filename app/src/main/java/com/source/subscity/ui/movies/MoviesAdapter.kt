package com.source.subscity.ui.movies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.dagger.GlideApp
import com.source.subscity.dagger.SubsCityGlideModule
import com.source.subscity.widgets.transformations.Crop

/**
 * @author Vitaliy Markus
 */
class MoviesAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val moviePoster = view.findViewById<ImageView>(R.id.iv_movie_poster)
        private val shadow = view.findViewById<View>(R.id.shadow)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_movie_language)
        private val movieStar = view.findViewById<ImageView>(R.id.iv_star)
        private val movieRating = view.findViewById<TextView>(R.id.tv_movie_rating)
        private val movieName = view.findViewById<TextView>(R.id.tv_movie_name)

        fun bind(movie: Movie) {
            GlideApp.with(moviePoster).load(movie.poster).transform(Crop()).into(moviePoster)
            movieLanguage.text = movie.languages.firstOrNull()
            movieRating.text = movie.commonRaiting.toString()
            movieName.text = movie.title.russian
        }
    }
}