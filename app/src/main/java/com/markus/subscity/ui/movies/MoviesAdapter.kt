package com.markus.subscity.ui.movies

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.markus.subscity.R
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.dagger.GlideApp
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.getWidthScreen
import com.markus.subscity.providers.DisplayLanguageProvider
import com.markus.subscity.providers.LanguageProvider
import com.markus.subscity.providers.isRussian
import com.markus.subscity.widgets.transformations.PosterCrop
import javax.inject.Inject


/**
 * @author Vitaliy Markus
 */
class MoviesAdapter(context: Context,
                    private val movies: List<Movie>,
                    private val clickListener: (Movie) -> Unit) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    companion object {
        private const val RATING = 6.9
    }

    private val errorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_error_poster)

    private val layoutInflater = LayoutInflater.from(context)
    private val width: Int = context.getWidthScreen()
    private val isFullSpans: MutableList<Boolean> = ArrayList()

    @Inject
    lateinit var languageProvider: LanguageProvider

    @Inject
    lateinit var displayLanguageProvider: DisplayLanguageProvider

    init {
        updateFullSpans()
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], isFullSpans[position])
    }

    private fun updateFullSpans() {
        movies.forEach { movie ->
            if (movie.commonRating > RATING && isFullSpans.count { !it } % 2 == 0) {
                isFullSpans.add(true)
            } else {
                isFullSpans.add(false)
            }
        }
        if (isFullSpans.count { !it } % 2 != 0) {
            isFullSpans[movies.lastIndex] = true
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var movie: Movie

        private val moviePoster = view.findViewById<ImageView>(R.id.iv_movie_poster)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_movie_language)
        private val movieName = view.findViewById<AppCompatTextView>(R.id.tv_movie_name)
        private val movieGenre = view.findViewById<TextView>(R.id.tv_movie_genre)

        init {
            view.setOnClickListener { clickListener.invoke(movie) }
        }

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie, isFullSpan: Boolean) {
            this.movie = movie

            val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = isFullSpan

            val posterWidth = if (isFullSpan) width else width / 2
            val maxLines = if (isFullSpan) R.integer.poster_max_line_huge else R.integer.poster_max_line
            val sizes = if (isFullSpan)
                intArrayOf(R.dimen.poster_text_size_middle, R.dimen.poster_text_size_large, R.dimen.poster_text_size_huge)
            else
                intArrayOf(R.dimen.poster_text_size)

            GlideApp.with(moviePoster)
                    .asBitmap()
                    .load(movie.poster)
                    .error(errorDrawable)
                    .override(posterWidth, layoutParams.height)
                    .transform(PosterCrop())
                    .into(moviePoster)

            movieLanguage.text = movieLanguage(movie)
            movieLanguage.visibility = if (movieLanguage.text.isNotEmpty()) View.VISIBLE else View.GONE

            movieGenre.text = movie.genres.joinToString(", ").capitalize()

            movieName.text = if (displayLanguageProvider.isRussian) movie.title.russian else movie.title.original
            movieName.context.resources.also { resources ->
                movieName.maxLines = resources.getInteger(maxLines)
                val dimensions = sizes.map { resources.getDimension(it).toInt() }.toIntArray()
                TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(movieName, dimensions, TypedValue.COMPLEX_UNIT_PX)
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