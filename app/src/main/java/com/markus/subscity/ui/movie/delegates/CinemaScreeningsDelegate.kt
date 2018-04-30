package com.markus.subscity.ui.movie.delegates

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
import com.markus.subscity.providers.MetroProvider
import com.markus.subscity.ui.cinema.CinemaPresenter
import com.markus.subscity.ui.movie.MoviePresenter
import com.markus.subscity.ui.movie.MovieScreeningAdapter
import com.markus.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaScreeningsDelegate(private val screeningClickListener: (Screening) -> Unit) : AbsListItemAdapterDelegate<MoviePresenter.CinemaScreenings, Any, CinemaScreeningsDelegate.CinemaScreeningsViewHolder>() {


    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): CinemaScreeningsDelegate.CinemaScreeningsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
        return CinemaScreeningsViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is MoviePresenter.CinemaScreenings
    }

    override fun onBindViewHolder(item: MoviePresenter.CinemaScreenings, viewHolder: CinemaScreeningsDelegate.CinemaScreeningsViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class CinemaScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val SPAN_COUNT = 5
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