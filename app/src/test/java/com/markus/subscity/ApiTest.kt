package com.markus.subscity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.deserializers.DateTimeDeserializer
import com.markus.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.helper.TestSchedulerRule
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * @author Vitaliy Markus
 */
class ApiTest {

    @Rule
    @JvmField
    var schedulerRule = TestSchedulerRule()

//    private val subsCityService = ApiClient(configureOkHttp(), configureGson(), schedulerRule.mainThreadScheduler).subsCityService

//    @Test
//    fun `backend returns movies`() {
//        Singles.zip(subsCityService.getMovies("spb"), subsCityService.getMovies("msk"))
//                .map { (spbMovies, mskMovies) -> spbMovies + mskMovies }
//                .map { movies -> movies.sortedBy { it.id }.distinctBy { it.id } }
//                .test()
//    }
//
//    @Test
//    fun `movies from a backend are the same as movies from all cinemas in a city`() {
//        val movieIdsRequest = subsCityService.getCinemas("spb").map { cinemas ->
//            cinemas.flatMap { it.movies }.distinct().sorted()
//        }
//        val filmIdsRequest = subsCityService.getMovies("spb").map { films -> films.map { it.id }.sorted() }
//        val testObserver = Singles.zip(movieIdsRequest, filmIdsRequest)
//                .map { (movieIds, filmIds) -> movieIds == filmIds }
//                .test()
//        assertTrue(testObserver.values().size == 1)
//        assertTrue(testObserver.values().first())
//
//    }
//
//    @Test
//    fun `every screening connected with a cinema which has a movie from a backend`() {
//        val selector: (Movie?) -> Long? = { movie -> movie?.id }
//        val moviesRequest = subsCityService.getMovies("spb")
//                .map { movies -> movies.sortedBy(selector) }
//                .toObservable()
//                .cache()
//        val screeningsRequest = subsCityService.getCinemas("spb")
//                .flattenAsObservable { it }
//                .flatMapSingle { cinema -> subsCityService.getCinemaScreenings("spb", cinema.id) }
//                .flatMapIterable { it }
//        val filmsRequest = screeningsRequest.flatMap { screening ->
//            moviesRequest.map { movies -> movies.find { movie -> movie.id == screening.movieId } }
//        }
//                .distinct()
//                .toList()
//                .map { films -> films.sortedBy(selector) }
//                .toObservable()
//        val testObserver = Observables.zip(moviesRequest, filmsRequest)
//                .map { (movies, films) -> movies == films }
//                .test()
//        assertTrue(testObserver.values().size == 1)
//        assertTrue(testObserver.values().first())
//    }
//
//    @Test
//    fun `every screening is connected with a movie which has a cinema from a backend`() {
//        val selector: (Cinema?) -> Long? = { cinema -> cinema?.id }
//        val cinemaRequest = subsCityService.getCinemas("spb")
//                .map { movies -> movies.sortedBy(selector) }
//                .toObservable()
//                .cache()
//        val screeningsRequest = subsCityService.getMovies("spb")
//                .flattenAsObservable { it }
//                .flatMapSingle { movie -> subsCityService.getMovieScreenings("spb", movie.id) }
//                .flatMapIterable { it }
//        val theaterRequest = screeningsRequest.flatMap { screening ->
//            cinemaRequest.map { cinemas -> cinemas.find { cinema -> cinema.id == screening.cinemaId } }
//        }
//                .distinct()
//                .toList()
//                .map { films -> films.sortedBy(selector) }
//                .toObservable()
//        val testObserver = Observables.zip(cinemaRequest, theaterRequest)
//                .map { (cinemas, theaters) -> cinemas == theaters }
//                .test()
//        assertTrue(testObserver.values().size == 1)
//        assertTrue(testObserver.values().first())
//    }
//
//    @Test
//    fun `backend returns screening after today right now`() {
//        val testObserver = subsCityService.getDateScreenings("spb", DateTime.now())
//                .test()
//        assertTrue(testObserver.values().size == 1)
//        assertTrue(testObserver.values().first().isNotEmpty())
//    }
//
//    @Test
//    fun `screenings from a movie are the same as screenings from a backend`() {
//        val testObserver = subsCityService.getMovies("spb")
//                .flattenAsObservable { it }
//                .flatMapSingle { movie ->
//                    subsCityService.getMovieScreenings("spb", movie.id)
//                            .map { screenings -> screenings.size == movie.screenings.count }
//                }
//                .all { it }
//                .test()
//        assertTrue(testObserver.values().size == 1)
//        assertTrue(testObserver.values().first())
//    }
//
//    private fun configureOkHttp(): OkHttpClient {
//        return OkHttpClient.Builder()
//                .addInterceptor(HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                })
//                .build()
//    }
//
//    private fun configureGson(): Gson {
//        return GsonBuilder()
//                .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer)
//                .registerTypeAdapterFactory(SubsCityTypeAdapterFactory)
//                .create()
//    }
}