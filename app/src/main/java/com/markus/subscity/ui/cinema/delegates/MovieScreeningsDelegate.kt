package com.markus.subscity.ui.cinema.delegates

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
class MovieScreeningsDelegate(private val screeningClickListener: (Screening) -> Unit) : AbsListItemAdapterDelegate<CinemaPresenter.MovieScreenings, Any, MovieScreeningsDelegate.MovieScreeningsViewHolder>() {

    @Inject
    lateinit var languageProvider: LanguageProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): MovieScreeningsDelegate.MovieScreeningsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
        return MovieScreeningsViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is CinemaPresenter.MovieScreenings
    }

    override fun onBindViewHolder(item: CinemaPresenter.MovieScreenings, viewHolder: MovieScreeningsDelegate.MovieScreeningsViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class MovieScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val SPAN_COUNT = 5
        private val movieTitle = view.findViewById<TextView>(R.id.tv_title)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        fun bind(movieScreenings: CinemaPresenter.MovieScreenings) {
            movieTitle.text = movieScreenings.movie.title.russian
            movieLanguage.text = movieLanguage(movieScreenings.movie)
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context!!, movieScreenings.screenings, screeningClickListener)
                val margin = context.resources.getDimensionPixelSize(R.dimen.screening_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, margin, false))
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

}