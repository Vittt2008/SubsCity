package com.markus.subscity.ui.cinema

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.repositories.MovieRepository
import com.markus.subscity.repositories.ScreeningRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class CinemaPresenter @Inject constructor(private val cinemaRepository: CinemaRepository,
                                          private val movieRepository: MovieRepository,
                                          private val screeningRepository: ScreeningRepository) : MvpPresenter<CinemaView>() {

    var cinemaId: Long = 0

    override fun onFirstViewAttach() {
        val cinema = cinemaRepository.getCinema(cinemaId)
                .subscribeOn(Schedulers.io())
                .toObservable()
        val movieScreenings: Observable<List<MovieScreenings>> = Singles.zip(screeningRepository.getCinemaScreenings(cinemaId), movieRepository.getMovies())
                .map(this::convert)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .startWith(emptyList<MovieScreenings>())

        Observables.combineLatest(cinema, movieScreenings)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.showCinema(it.first, it.second) },
                        { viewState.onError(it) }
                )
    }

    private fun convert(pair: Pair<List<Screening>, List<Movie>>): List<MovieScreenings> {
        val now = DateTime.now()
        val screenings = pair.first.filter { x -> x.dateTime > now }
        val movies = pair.second.associateBy { it.id }
        val movieScreenings = screenings
                .filter { it.movieId != 0L }
                .groupBy { it.movieId }
                .filter { movies.containsKey(it.key) } //TODO Если ключ не найден, то нужно перезапросить все с сервера
                .map { MovieScreenings(movies.getValue(it.key), it.value.sortedBy { x -> x.dateTime }) }
        return movieScreenings
    }

    class MovieScreenings(val movie: Movie,
                          val screenings: List<Screening>)
}