package com.markus.subscity.ui.cinema.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.LanguageProvider
import com.markus.subscity.ui.cinema.CinemaPresenter
import com.markus.subscity.ui.movie.MovieScreeningAdapter
import com.markus.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieScreeningsDelegate(private val screeningClickListener: (Screening) -> Unit,
                              private val movieTitleClickListener: (Movie) -> Unit) : AbsListItemAdapterDelegate<CinemaPresenter.MovieScreenings, Any, MovieScreeningsDelegate.MovieScreeningsViewHolder>() {

    @Inject
    lateinit var languageProvider: LanguageProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): MovieScreeningsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
        return MovieScreeningsViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is CinemaPresenter.MovieScreenings
    }

    override fun onBindViewHolder(item: CinemaPresenter.MovieScreenings, viewHolder: MovieScreeningsViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class MovieScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val SPAN_COUNT = 4
        private lateinit var movie: Movie
        private val titleLayout = view.findViewById<View>(R.id.title_layout)
        private val movieTitle = view.findViewById<TextView>(R.id.tv_title)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        init {
            titleLayout.setOnClickListener { movieTitleClickListener.invoke(movie) }
        }

        fun bind(movieScreenings: CinemaPresenter.MovieScreenings) {
            movie = movieScreenings.movie
            movieTitle.text = movie.title.russian
            movieLanguage.text = movieLanguage()
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context, movieScreenings.screenings, SPAN_COUNT, screeningClickListener)
                val horizontal = context.resources.getDimensionPixelSize(R.dimen.screening_horizontal_margin)
                val vertical = context.resources.getDimensionPixelSize(R.dimen.screening_vertical_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, horizontal, vertical, false))
            }
        }

        private fun movieLanguage(): String {
            val movieLanguage = languageProvider.languageFormat(movie)
            val age = movie.ageRestriction.toString() + "+"
            if (movieLanguage.isNullOrEmpty()) {
                return age
            }
            return "$movieLanguage, $age"
        }
    }

}