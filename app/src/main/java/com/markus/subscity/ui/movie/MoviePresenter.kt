package com.markus.subscity.ui.movie

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.providers.DateTimeProvider
import com.markus.subscity.providers.DisplayLanguageProvider
import com.markus.subscity.providers.isRussian
import com.markus.subscity.repositories.CinemaRepository
import com.markus.subscity.repositories.MovieRepository
import com.markus.subscity.repositories.ScreeningRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class MoviePresenter @Inject constructor(private val movieRepository: MovieRepository,
                                         private val cinemaRepository: CinemaRepository,
                                         private val screeningRepository: ScreeningRepository,
                                         private val dateTimeProvider: DateTimeProvider,
                                         private val displayLanguageProvider: DisplayLanguageProvider) : MvpPresenter<MovieView>() {

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
                .subscribe({ (movie, screenings) ->
                    val title = if (displayLanguageProvider.isRussian) movie.title.russian else movie.title.original
                    viewState.showTitle(title)
                    viewState.showMovie(movie, screenings)
                }, {
                    viewState.onError(it)
                })
    }

    private fun convert(pair: Pair<List<Screening>, List<Cinema>>): List<CinemaScreenings> {
        val now = dateTimeProvider.now()
        val screenings = pair.first.filter { x -> x.dateTime > now }
        val cinemas = pair.second.associateBy { it.id }
        return screenings
                .filter { it.cinemaId != 0L }
                .groupBy { it.cinemaId }
                .filter { cinemas.containsKey(it.key) } //TODO If key isn't exist, we would have to reload all data from backend
                .map { CinemaScreenings(cinemas.getValue(it.key), it.value.sortedBy { x -> x.dateTime }) }
    }

    class CinemaScreenings(val cinema: Cinema,
                           val screenings: List<Screening>)
}