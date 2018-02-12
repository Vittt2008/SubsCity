package com.source.subscity.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.source.subscity.api.client.converters.StringConverterFactory
import com.source.subscity.api.deserializers.DateTimeDeserializer
import com.source.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.services.SubsCityService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang3.reflect.TypeUtils
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.junit.Test
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * @author Vitaliy Markus
 */
class ApiTest {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost")
            .client(configureOkHttp())
            .addConverterFactory(StringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(configureGson()))
            .addCallAdapterFactory(configureCallAdapterFactory())
            .build()

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

    fun configureCallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Test
    fun getMovies() {
        val subsCityService = retrofit.create(SubsCityService::class.java)
        val spbList = subsCityService.getMovies("spb").blockingGet()
        val mskList = subsCityService.getMovies("msk").blockingGet()
        val allMovie = ArrayList<Movie>().apply { addAll(spbList); addAll(mskList) }.sortedBy { it.id }
        val distinct = allMovie.distinctBy { it.id }
        assert(true)
    }

    @Test
    fun getMovieScreening() {
        val subsCityService = retrofit.create(SubsCityService::class.java)
        val allScreeningSuccess = subsCityService.getMovies("spb")
                .flattenAsObservable { it }
                .map { subsCityService.getMovieScreenings("spb", it.id).blockingGet().size == it.screenings.count }
                .all { it }
                .blockingGet()
        assert(allScreeningSuccess)
    }

    @Test
    fun getCinemas() {
        val subsCityService = retrofit.create(SubsCityService::class.java)
        val movies = subsCityService.getMovies("spb").blockingGet()
        val cinemas = subsCityService.getCinemas("spb").blockingGet()
        val films = cinemas.flatMap { it.movies }.distinct().map { id -> movies.first { movie -> movie.id == id } }
        assert(movies.size == films.size)
    }

    @Test
    fun getCinemaScreening() {
        val subsCityService = retrofit.create(SubsCityService::class.java)
        val movies = subsCityService.getMovies("spb").blockingGet()
        val films = subsCityService.getCinemas("spb")
                .flattenAsObservable { it }
                .flatMapSingle { subsCityService.geCinemaScreenings("spb", it.id) }
                .flatMapIterable { it }
                .map { screening -> movies.first { movie -> movie.id == screening.movieId } }
                .distinct()
                .toList()
                .blockingGet()
        assert(movies.size == films.size)
    }

    @Test
    fun getDataScreenings() {
        val subsCityService = retrofit.create(SubsCityService::class.java)
        val list = subsCityService.geDateScreenings("spb", LocalDate.now()).blockingGet()
        assert(true)
    }
}