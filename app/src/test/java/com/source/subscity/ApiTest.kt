package com.source.subscity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.source.subscity.api.client.SubsCityClient
import com.source.subscity.api.deserializers.DateTimeDeserializer
import com.source.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.source.subscity.api.entities.movie.Movie
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.junit.Test

/**
 * @author Vitaliy Markus
 */
class ApiTest {

    private val subsCityClient = SubsCityClient(configureOkHttp(), configureGson(), Schedulers.single())

    fun configureOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
    }

    fun configureGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer)
                .registerTypeAdapterFactory(SubsCityTypeAdapterFactory)
                .serializeNulls()
                .create()
    }


    @Test
    fun getMovies() {
        val spbList = subsCityClient.subsCityService.getMovies("spb").blockingGet()
        val mskList = subsCityClient.subsCityService.getMovies("msk").blockingGet()
        val allMovie = ArrayList<Movie>().apply { addAll(spbList); addAll(mskList) }.sortedBy { it.id }
        val distinct = allMovie.distinctBy { it.id }
        assert(true)
    }

    @Test
    fun getMovieScreening() {
        val allScreeningSuccess = subsCityClient.subsCityService.getMovies("spb")
                .flattenAsObservable { it }
                .map { subsCityClient.subsCityService.getMovieScreenings("spb", it.id).blockingGet().size == it.screenings.count }
                .all { it }
                .blockingGet()
        assert(allScreeningSuccess)
    }

    @Test
    fun getCinemas() {
        val movies = subsCityClient.subsCityService.getMovies("spb").blockingGet()
        val cinemas = subsCityClient.subsCityService.getCinemas("spb").blockingGet()
        val films = cinemas.flatMap { it.movies }.distinct().map { id -> movies.first { movie -> movie.id == id } }
        assert(movies.size == films.size)
    }

    @Test
    fun getCinemaScreening() {
        val movies = subsCityClient.subsCityService.getMovies("spb").blockingGet()
        val films = subsCityClient.subsCityService.getCinemas("spb")
                .flattenAsObservable { it }
                .flatMapSingle { subsCityClient.subsCityService.geCinemaScreenings("spb", it.id) }
                .flatMapIterable { it }
                .map { screening -> movies.first { movie -> movie.id == screening.movieId } }
                .distinct()
                .toList()
                .blockingGet()
        assert(movies.size == films.size)
    }

    @Test
    fun getDataScreenings() {
        val list = subsCityClient.subsCityService.geDateScreenings("spb", LocalDate.now()).blockingGet()
        assert(true)
    }
}