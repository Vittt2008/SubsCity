package com.markus.subscity.ui.movie.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.MetroProvider
import com.markus.subscity.ui.movie.MoviePresenter
import com.markus.subscity.ui.movie.MovieScreeningAdapter
import com.markus.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaScreeningsDelegate(private val screeningClickListener: (Screening) -> Unit,
                               private val cinemaTitleClickListener: (Cinema) -> Unit) : AbsListItemAdapterDelegate<MoviePresenter.CinemaScreenings, Any, CinemaScreeningsDelegate.CinemaScreeningsViewHolder>() {

    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): CinemaScreeningsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
        return CinemaScreeningsViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is MoviePresenter.CinemaScreenings
    }

    override fun onBindViewHolder(item: MoviePresenter.CinemaScreenings, viewHolder: CinemaScreeningsViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class CinemaScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val SPAN_COUNT = 5
        private lateinit var cinema: Cinema
        private val titleLayout = view.findViewById<View>(R.id.title_layout)
        private val cinemaTitle = view.findViewById<TextView>(R.id.tv_title)
        private val cinemaMetro = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        init {
            titleLayout.setOnClickListener { cinemaTitleClickListener.invoke(cinema) }
        }

        fun bind(cinemaScreenings: MoviePresenter.CinemaScreenings) {
            cinema = cinemaScreenings.cinema
            cinemaTitle.text = cinema.name
            cinemaMetro.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context!!, cinemaScreenings.screenings, screeningClickListener)
                val margin = context.resources.getDimensionPixelSize(R.dimen.screening_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, margin, false))
            }
        }
    }

}