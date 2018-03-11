package com.source.subscity.ui.movie

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.repositories.CinemaRepository
import com.source.subscity.repositories.MovieRepository
import com.source.subscity.repositories.ScreeningRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviePresenter @Inject constructor(private val movieRepository: MovieRepository,
                                         private val cinemaRepository: CinemaRepository,
                                         private val screeningRepository: ScreeningRepository) : MvpPresenter<MovieView>() {

    var movieId: Long = 0

    override fun onFirstViewAttach() {
        val movie = movieRepository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .toObservable()
        val cinemaScreenings: Observable<List<CinemaScreenings>> = Singles.zip(screeningRepository.getMovieScreenings(movieId), cinemaRepository.getCinemas())
                .map(this::convert)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .startWith(emptyList<CinemaScreenings>())

        Observables.combineLatest(movie, cinemaScreenings)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showMovie(it.first, it.second) },
                        { viewState.onError(it) }
                )
    }

    private fun convert(pair: Pair<List<Screening>, List<Cinema>>): List<CinemaScreenings> {
        val screenings = pair.first
        val cinemas = pair.second.associateBy { it.id }
        val cinemaScreenings = screenings
                .filter { it.cinemaId != 0L }
                .groupBy { it.cinemaId }
                .map { CinemaScreenings(cinemas.getValue(it.key), it.value.sortedBy { x -> x.dateTime }) }
        return cinemaScreenings
    }

    class CinemaScreenings(val cinema: Cinema,
                           val screenings: List<Screening>)
}