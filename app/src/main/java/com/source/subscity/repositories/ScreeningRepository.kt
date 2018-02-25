package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.db.DatabaseClient
import io.reactivex.Single
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class ScreeningRepository @Inject constructor(apiClient: ApiClient, databaseClient: DatabaseClient) :
        CachedRepository(apiClient, databaseClient) {

    private val city = "spb"

    private val movieScreeningKey = "movie_screening"
    private val cinemaScreeningKey = "cinema_screening"
    private val dateScreeningKey = "date_screening"

    fun getMovieScreenings(movieId: Long): Single<List<Screening>> {
        return isCacheActual(movieScreeningKey)
                .flatMap { isActual ->
                    if (isActual) {
                        getMovieScreeningsFromDb(movieId)
                    } else {
                        getMovieScreeningsFromApi(movieId)
                                .onErrorResumeNext(getMovieScreeningsFromDb(movieId))
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }

    fun getCinemaScreenings(cinemaId: Long): Single<List<Screening>> {
        return isCacheActual(cinemaScreeningKey)
                .flatMap { isActual ->
                    if (isActual) {
                        getCinemaScreeningsFromDb(cinemaId)
                    } else {
                        getCinemaScreeningsFromApi(cinemaId)
                                .onErrorResumeNext(getCinemaScreeningsFromDb(cinemaId))
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }

    fun getDateScreenings(dateTime: DateTime): Single<List<Screening>> {
        return isCacheActual(dateScreeningKey)
                .flatMap { isActual ->
                    if (isActual) {
                        getDateScreeningsFromDb(dateTime)
                    } else {
                        getDateScreeningsFromApi(dateTime)
                                .onErrorResumeNext(getDateScreeningsFromDb(dateTime))
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }


    override fun getCacheLifetime() = TimeUnit.DAYS.toMillis(1)

    private fun getMovieScreeningsFromApi(movieId: Long): Single<List<Screening>> {
        return apiClient.subsCityService.getMovieScreenings(city, movieId)
                .doOnSuccess { it -> databaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(movieScreeningKey) }
    }

    private fun getMovieScreeningsFromDb(movieId: Long): Single<List<Screening>> {
        return databaseClient.screeningDao.getMovieScreenings(movieId)
                .firstOrError()
    }

    private fun getCinemaScreeningsFromApi(cinemaId: Long): Single<List<Screening>> {
        return apiClient.subsCityService.getCinemaScreenings(city, cinemaId)
                .doOnSuccess { it -> databaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(cinemaScreeningKey) }
    }

    private fun getCinemaScreeningsFromDb(cinemaId: Long): Single<List<Screening>> {
        return databaseClient.screeningDao.getCinemaScreenings(cinemaId)
                .firstOrError()
    }

    private fun getDateScreeningsFromApi(dateTime: DateTime): Single<List<Screening>> {
        return apiClient.subsCityService.getDateScreenings(city, dateTime)
                .doOnSuccess { it -> databaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(dateScreeningKey) }
    }

    private fun getDateScreeningsFromDb(dateTime: DateTime): Single<List<Screening>> {
        val from = dateTime.withTimeAtStartOfDay()
        val to = from.plusDays(1)
        return databaseClient.screeningDao.getDateScreenings(from, to)
                .firstOrError()
    }
}