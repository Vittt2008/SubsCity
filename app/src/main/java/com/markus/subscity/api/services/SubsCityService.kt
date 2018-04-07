package com.markus.subscity.api.services

import com.markus.subscity.api.annotations.OnlyDate
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import io.reactivex.Single
import org.joda.time.DateTime
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Vitaliy Markus
 */
interface SubsCityService {

    @GET("https://{city}.subscity.ru/movies.json")
    fun getMovies(@Path("city") city: String): Single<List<Movie>>

    @GET("https://{city}.subscity.ru/cinemas.json")
    fun getCinemas(@Path("city") city: String): Single<List<Cinema>>

    @GET("https://{city}.subscity.ru/movies/screenings/{movie_id}.json")
    fun getMovieScreenings(@Path("city") city: String, @Path("movie_id") movieId: Long): Single<List<Screening>>

    @GET("https://{city}.subscity.ru/cinemas/screenings/{cinema_id}.json")
    fun getCinemaScreenings(@Path("city") city: String, @Path("cinema_id") cinemaId: Long): Single<List<Screening>>

    @GET("https://{city}.subscity.ru/dates/screenings/{date}.json")
    fun getDateScreenings(@Path("city") city: String, @Path("date") @OnlyDate date: DateTime): Single<List<Screening>>

}