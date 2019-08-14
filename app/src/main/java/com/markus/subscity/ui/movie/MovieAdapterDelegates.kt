package com.markus.subscity.ui.movie

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.ui.cinema.delegates.ProgressDelegate
import com.markus.subscity.ui.cinema.delegates.TitleDelegate
import com.markus.subscity.ui.movie.delegates.CinemaScreeningsDelegate
import com.markus.subscity.ui.movie.delegates.MovieInfoDelegate

/**
 * @author Vitaliy Markus
 */
class MovieAdapterDelegates(val movie: Movie,
                            cinemaScreenings: List<MoviePresenter.CinemaScreenings>,
                            screeningClickListener: (Screening) -> Unit,
                            cinemaTitleClickListener: (Cinema) -> Unit) : ListDelegationAdapter<List<Any>>() {

    private val data: MutableList<Any> = ArrayList()

    init {
        delegatesManager.addDelegate(MovieInfoDelegate())
                .addDelegate(TitleDelegate())
                .addDelegate(ProgressDelegate())
                .addDelegate(CinemaScreeningsDelegate(screeningClickListener, cinemaTitleClickListener))

        data.add(movie)
        if (cinemaScreenings.isNotEmpty()) {
            data.add(TitleDelegate.TitleInfo(R.string.movie_screenings_title))
            data.addAll(cinemaScreenings)
        } else {
            data.add(ProgressDelegate.ProgressObject)
        }
        setItems(data)
    }

    fun updateScreenings(cinemaScreenings: List<MoviePresenter.CinemaScreenings>) {
        val oldSize = itemCount
        if (cinemaScreenings.isNotEmpty()) {
            data[oldSize - 1] = TitleDelegate.TitleInfo(R.string.movie_screenings_title)
            notifyItemChanged(oldSize - 1)
            data.addAll(cinemaScreenings)
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        } else {
            data.removeAt(oldSize - 1)
            notifyItemRemoved(oldSize - 1)
        }
    }
}