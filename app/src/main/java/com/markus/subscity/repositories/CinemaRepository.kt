package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.extensions.timeout
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.DatabaseProvider
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaRepository @Inject constructor(apiClient: ApiClient,
                                           databaseProvider: DatabaseProvider,
                                           private val cityProvider: CityProvider) : CachedRepository(apiClient, databaseProvider) {

    fun getCinemas(): Single<List<Cinema>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getCinemasFromDb()
                    } else {
                        getCinemaFromApi()
                                .onErrorResumeNext { getCinemasFromDb() }
                    }
                }
                .map { cinema -> cinema.sortedBy { it.name } }
    }

    fun getCinema(id: Long): Single<Cinema> {
        return databaseProvider.currentDatabaseClient.cinemaDao.getCinema(id)
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
        return apiClient.subsCityService.getCinemas(cityProvider.cityId)
                .doOnSuccess { databaseProvider.currentDatabaseClient.cinemaDao.deleteAllCinemas() } //TODO Remove All
                .doOnSuccess { it -> databaseProvider.currentDatabaseClient.cinemaDao.saveCinemas(it) }
                .doOnSuccess { updateCacheTimestamp() }
                .timeout()
    }

    private fun getCinemasFromDb(): Single<List<Cinema>> {
        return databaseProvider.currentDatabaseClient.cinemaDao.getAllCinemas()
                .firstOrError()
    }
}