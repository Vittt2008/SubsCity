package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.extensions.timeout
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.DatabaseProvider
import com.markus.subscity.providers.DateTimeProvider
import io.reactivex.Single
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx2.asObservable
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaRepository @Inject constructor(private val apiClient: ApiClient,
                                           databaseProvider: DatabaseProvider,
                                           private val cityProvider: CityProvider,
                                           dateTimeProvider: DateTimeProvider) : CachedRepository(databaseProvider, dateTimeProvider) {

    fun getCinemas(): Single<List<Cinema>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getCinemasFromDb().asObservable().firstOrError()
                    } else {
                        getCinemaFromApi().asObservable().firstOrError()
                                .onErrorResumeNext { getCinemasFromDb().asObservable().firstOrError() }
                    }
                }
                .map { cinema -> cinema.sortedBy { it.name } }
    }

    fun getCinemasFlow(): Flow<List<Cinema>> {
        return isCacheActual().toFlowable().asFlow()
                .flatMapConcat { isActual ->
                    if (isActual) {
                        getCinemasFromDb()
                    } else {
                        getCinemaFromApi()
                                .catch { emitAll(getCinemasFromDb()) }

                    }
                }
                .map { cinema -> cinema.sortedBy { it.name } }
    }

    fun getCinema(id: Long): Single<Cinema> {
        return databaseProvider.currentDatabaseClient.cinemaDao.getCinema(id)
                .onErrorResumeNext {
                    getCinemaFromApi()
                            .asObservable()
                            .flatMapIterable { it }
                            .filter { it.id == id }
                            .singleOrError()
                }
    }

    override fun getDefaultCacheKey() = "cinema"

    override fun getSyncTime() = arrayOf("10:00", "14:00", "22:00")

    private fun getCinemaFromApi(): Flow<List<Cinema>> {
        return apiClient.subsCityService.getCinemas(cityProvider.cityId)
                .onEach { databaseProvider.currentDatabaseClient.cinemaDao.deleteAllCinemas() }
                .onEach { databaseProvider.currentDatabaseClient.cinemaDao.saveCinemas(it) }
                .onEach { updateCacheTimestamp() }
    }

    private fun getCinemasFromDb(): Flow<List<Cinema>> {
        return databaseProvider.currentDatabaseClient.cinemaDao.getAllCinemas().asFlow()
    }
}