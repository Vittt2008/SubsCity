package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.extensions.timeout
import com.source.subscity.providers.CityProvider
import com.source.subscity.providers.DatabaseProvider
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieRepository @Inject constructor(apiClient: ApiClient,
                                          databaseProvider: DatabaseProvider,
                                          private val cityProvider: CityProvider) : CachedRepository(apiClient, databaseProvider) {

    fun getMovies(): Single<List<Movie>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getMoviesFromDb()
                    } else {
                        getMoviesFromApi()
                                .onErrorResumeNext { getMoviesFromDb() }
                    }
                }
                .map { movies -> movies.sortedBy { it.title.russian } }
    }

    fun getMovie(id: Long): Single<Movie> {
        return databaseProvider.currentDatabaseClient.movieDao.getMovie(id)
                .onErrorResumeNext {
                    getMoviesFromApi()
                            .toObservable()
                            .flatMapIterable { it }
                            .filter { it.id == id }
                            .singleOrError()
                }
    }

    override fun getDefaultCacheKey() = "movie"

    override fun getCacheLifetime() = TimeUnit.DAYS.toMillis(1)

    private fun getMoviesFromApi(): Single<List<Movie>> {
        return apiClient.subsCityService.getMovies(cityProvider.cityId)
                .doOnSuccess { databaseProvider.currentDatabaseClient.movieDao.deleteAllMovies() } //TODO Remove All
                .doOnSuccess { it -> databaseProvider.currentDatabaseClient.movieDao.saveMovies(it) }
                .doOnSuccess { updateCacheTimestamp() }
                .timeout()
    }

    private fun getMoviesFromDb(): Single<List<Movie>> {
        return databaseProvider.currentDatabaseClient.movieDao.getAllMovies()
                .firstOrError()
    }
}