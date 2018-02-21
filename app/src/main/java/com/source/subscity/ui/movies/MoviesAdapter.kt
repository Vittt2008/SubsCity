package com.source.subscity.ui.movies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.dagger.GlideApp
import com.source.subscity.widgets.transformations.Crop


/**
 * @author Vitaliy Markus
 */
class MoviesAdapter(context: Context, private val movies: List<Movie>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private val width: Int

    init {
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val moviePoster = view.findViewById<ImageView>(R.id.iv_movie_poster)
        private val shadow = view.findViewById<View>(R.id.shadow)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_movie_language)
        private val movieStar = view.findViewById<ImageView>(R.id.iv_star)
        private val movieRating = view.findViewById<TextView>(R.id.tv_movie_rating)
        private val movieName = view.findViewById<TextView>(R.id.tv_movie_name)

        fun bind(movie: Movie) {
            val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = movie.commonRating > 7

            val posterWidth = if (layoutParams.isFullSpan) width else width / 2
            val textSizeIdRes = if (layoutParams.isFullSpan) R.dimen.poster_text_size_huge else R.dimen.poster_text_size
            GlideApp.with(moviePoster).asBitmap().load(movie.poster).override(posterWidth, layoutParams.height).transform(Crop(movie.poster)).into(moviePoster)

            movieLanguage.text = movie.languages.firstOrNull()?.capitalize()
            movieLanguage.visibility = if (movieLanguage.text.isNotEmpty()) View.VISIBLE else View.GONE

            if (movie.commonRating != 0.0) {
                movieRating.text = String.format("%.${2}f", movie.commonRating)
                movieStar.visibility = View.VISIBLE
                movieRating.visibility = View.VISIBLE
            } else {
                movieStar.visibility = View.GONE
                movieRating.visibility = View.GONE
            }

            movieName.text = movie.title.russian
            movieName.setTextSize(TypedValue.COMPLEX_UNIT_PX, movieName.context.resources.getDimensionPixelSize(textSizeIdRes).toFloat())
        }
    }
}