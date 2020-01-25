package com.markus.subscity.ui.cinema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.providers.DateTimeProvider
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.repositories.MovieRepository
import com.markus.subscity.repositories.ScreeningRepository
import com.markus.subscity.viewmodels.BaseInitableViewModel
import com.markus.subscity.viewmodels.LiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CinemaViewModel @Inject constructor(private val cinemaRepository: CinemaRepository,
                                          private val movieRepository: MovieRepository,
                                          private val screeningRepository: ScreeningRepository,
                                          private val dateTimeProvider: DateTimeProvider) : BaseInitableViewModel() {

    private val cinemaEventInner = MutableLiveData<CinemaEvent>()

    val cinemaEvent: LiveData<CinemaEvent>
        get() = cinemaEventInner

    var cinemaId: Long = 0

    override fun doInit() {
        val cinema = cinemaRepository.getCinema(cinemaId)
                .subscribeOn(Schedulers.io())
                .toObservable()

        val movieScreenings: Observable<List<MovieScreenings>> = Singles.zip(screeningRepository.getCinemaScreenings(cinemaId), movieRepository.getMovies())
                .map(this::convert)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .startWith(emptyList<MovieScreenings>())

        Observables.combineLatest(cinema, movieScreenings) { cinema, screenings -> CinemaEvent(cinema, screenings) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTillDetach(cinemaEventInner::postValue, errorEventInner::postValue)
    }

    private fun convert(pair: Pair<List<Screening>, List<Movie>>): List<MovieScreenings> {
        val now = dateTimeProvider.now()
        val screenings = pair.first.filter { x -> x.dateTime > now }
        val movies = pair.second.associateBy { it.id }
        return screenings
                .filter { it.movieId != 0L }
                .groupBy { it.movieId }
                .filter { movies.containsKey(it.key) } //TODO If key isn't exist, we would have to reload all data from backend
                .map { MovieScreenings(movies.getValue(it.key), it.value.sortedBy { x -> x.dateTime }) }
    }

    class CinemaEvent(val cinema: Cinema,
                      val screenings: List<MovieScreenings>)

    class MovieScreenings(val movie: Movie,
                          val screenings: List<Screening>)
}