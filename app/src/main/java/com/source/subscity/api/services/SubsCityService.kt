package com.source.subscity.api.services

import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.CinemaScreening
import com.source.subscity.api.entities.screening.DateScreening
import com.source.subscity.api.entities.screening.MovieScreening
import io.reactivex.Single
import org.joda.time.LocalDate
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Vitaliy Markus
 */
interface SubsCityService {

    @GET("https://{city}.subscity.ru/movies.json")
    fun getMovies(@Path("city") city: String): Single<List<Movie>>

    @GET("https://{city}.subscity.ru/movies/screenings/{movie_id}.json")
    fun getMovieScreenings(@Path("city") city: String, @Path("movie_id") movieId: Long): Single<List<MovieScreening>>

    @GET("https://{city}.subscity.ru/cinemas.json")
    fun getCinemas(@Path("city") city: String): Single<List<Cinema>>

    @GET("https://{city}.subscity.ru/cinemas/screenings/{cinema_id}.json")
    fun geCinemaScreenings(@Path("city") city: String, @Path("cinema_id") cinemaId: Long): Single<List<CinemaScreening>>

    @GET("https://{city}.subscity.ru/dates/screenings/{date}.json")
    fun geDateScreenings(@Path("city") city: String, @Path("date") date: LocalDate): Single<List<DateScreening>>

}