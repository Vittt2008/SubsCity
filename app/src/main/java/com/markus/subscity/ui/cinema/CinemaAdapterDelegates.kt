package com.markus.subscity.ui.cinema

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.ui.cinema.delegates.CinemaInfoDelegate
import com.markus.subscity.ui.cinema.delegates.MovieScreeningsDelegate
import com.markus.subscity.ui.cinema.delegates.ProgressDelegate
import com.markus.subscity.ui.cinema.delegates.TitleDelegate

/**
 * @author Vitaliy Markus
 */
class CinemaAdapterDelegates(cinema: Cinema,
                             movieScreenings: List<CinemaPresenter.MovieScreenings>,
                             mapClickListener: (Cinema) -> Unit,
                             phoneClickListener: (Cinema) -> Unit,
                             siteClickListener: (Cinema) -> Unit,
                             screeningClickListener: (Screening) -> Unit,
                             movieTitleClickListener: (Movie) -> Unit) : ListDelegationAdapter<List<Any>>() {

    private val data: MutableList<Any> = ArrayList()

    init {
        delegatesManager.addDelegate(CinemaInfoDelegate(mapClickListener, phoneClickListener, siteClickListener))
                .addDelegate(TitleDelegate())
                .addDelegate(ProgressDelegate())
                .addDelegate(MovieScreeningsDelegate(screeningClickListener, movieTitleClickListener))

        data.add(cinema)
        if (movieScreenings.isNotEmpty()) {
            data.add(R.string.movie_screenings_title)
            data.addAll(movieScreenings)
        } else {
            data.add(Any())
        }
        setItems(data)
    }

    fun updateScreenings(movieScreenings: List<CinemaPresenter.MovieScreenings>) {
        val oldSize = itemCount
        if (movieScreenings.isNotEmpty()) {
            data[oldSize - 1] = R.string.movie_screenings_title
            notifyItemChanged(oldSize - 1)
            data.addAll(movieScreenings)
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        } else {
            data.removeAt(oldSize - 1)
            notifyItemRemoved(oldSize - 1)
        }
    }
}