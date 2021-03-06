package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.extensions.timeout
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.DatabaseProvider
import com.markus.subscity.providers.DateTimeProvider
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class ScreeningRepository @Inject constructor(private val apiClient: ApiClient,
                                              databaseProvider: DatabaseProvider,
                                              private val cityProvider: CityProvider,
                                              dateTimeProvider: DateTimeProvider) : CachedRepository(databaseProvider, dateTimeProvider) {

    private val movieScreeningKey = "movie_screening_"
    private val cinemaScreeningKey = "cinema_screening_"
    private val dateScreeningKey = "date_screening_"

    fun getMovieScreenings(movieId: Long): Single<List<Screening>> {
        return isCacheActual(movieScreeningKey + movieId)
                .flatMap { isActual ->
                    if (isActual) {
                        getMovieScreeningsFromDb(movieId)
                    } else {
                        getMovieScreeningsFromApi(movieId)
                                .onErrorResumeNext { getMovieScreeningsFromDb(movieId) }
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }

    fun getCinemaScreenings(cinemaId: Long): Single<List<Screening>> {
        return isCacheActual(cinemaScreeningKey + cinemaId)
                .flatMap { isActual ->
                    if (isActual) {
                        getCinemaScreeningsFromDb(cinemaId)
                    } else {
                        getCinemaScreeningsFromApi(cinemaId)
                                .onErrorResumeNext { getCinemaScreeningsFromDb(cinemaId) }
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }

    fun getDateScreenings(dateTime: DateTime): Single<List<Screening>> {
        return isCacheActual(dateScreeningKey + dateTime.toString())
                .flatMap { isActual ->
                    if (isActual) {
                        getDateScreeningsFromDb(dateTime)
                    } else {
                        getDateScreeningsFromApi(dateTime)
                                .onErrorResumeNext { getDateScreeningsFromDb(dateTime) }
                    }
                }
                .map { movies -> movies.sortedBy { it.dateTime.millis } }
    }


    override fun getSyncTime() = arrayOf("07:05", "11:20", "14:50", "17:40", "20:40", "00:05")

    private fun getMovieScreeningsFromApi(movieId: Long): Single<List<Screening>> {
        return apiClient.subsCityService.getMovieScreenings(cityProvider.cityId, movieId)
                .doOnSuccess { databaseProvider.currentDatabaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(movieScreeningKey) }
                .timeout()
    }

    private fun getMovieScreeningsFromDb(movieId: Long): Single<List<Screening>> {
        return databaseProvider.currentDatabaseClient.screeningDao.getMovieScreenings(movieId)
                .firstOrError()
    }

    private fun getCinemaScreeningsFromApi(cinemaId: Long): Single<List<Screening>> {
        return apiClient.subsCityService.getCinemaScreenings(cityProvider.cityId, cinemaId)
                .doOnSuccess { databaseProvider.currentDatabaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(cinemaScreeningKey) }
                .timeout()
    }

    private fun getCinemaScreeningsFromDb(cinemaId: Long): Single<List<Screening>> {
        return databaseProvider.currentDatabaseClient.screeningDao.getCinemaScreenings(cinemaId)
                .firstOrError()
    }

    private fun getDateScreeningsFromApi(dateTime: DateTime): Single<List<Screening>> {
        return apiClient.subsCityService.getDateScreenings(cityProvider.cityId, dateTime)
                .doOnSuccess { databaseProvider.currentDatabaseClient.screeningDao.saveScreening(it) }
                .doOnSuccess { updateCacheTimestamp(dateScreeningKey) }
                .timeout()
    }

    private fun getDateScreeningsFromDb(dateTime: DateTime): Single<List<Screening>> {
        val from = dateTime.withTimeAtStartOfDay()
        val to = from.plusDays(1)
        return databaseProvider.currentDatabaseClient.screeningDao.getDateScreenings(from, to)
                .firstOrError()
    }
}