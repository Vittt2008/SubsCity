package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.db.DatabaseClient
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaRepository @Inject constructor(apiClient: ApiClient, databaseClient: DatabaseClient) :
        CachedRepository(apiClient, databaseClient) {

    private val city = "spb"

    fun getCinemas(): Single<List<Cinema>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getCinemasFromDb()
                    } else {
                        getCinemaFromApi()
                                .onErrorResumeNext(getCinemasFromDb())
                    }
                }
                .map { cinema -> cinema.sortedBy { it.name } }
    }

    fun getCinema(id: Long): Single<Cinema> {
        return databaseClient.cinemaDao.getCinema(id)
                .onErrorResumeNext {
                    getCinemaFromApi()
                            .toObservable()
                            .flatMapIterable { it }
                            .filter { it.id == id }
                            .singleOrError()
                }
    }

    override fun getDefaultCacheKey() = "cinema"

    override fun getCacheLifetime() = TimeUnit.DAYS.toMillis(1)

    private fun getCinemaFromApi(): Single<List<Cinema>> {
        return apiClient.subsCityService.getCinemas(city)
                .doOnSuccess { it -> databaseClient.cinemaDao.saveCinemas(it) }
                .doOnSuccess { updateCacheTimestamp() }
    }

    private fun getCinemasFromDb(): Single<List<Cinema>> {
        return databaseClient.cinemaDao.getAllCinemas()
                .firstOrError()
    }
}