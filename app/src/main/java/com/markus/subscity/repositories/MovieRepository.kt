package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.extensions.timeout
import com.markus.subscity.providers.*
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieRepository @Inject constructor(private val apiClient: ApiClient,
                                          databaseProvider: DatabaseProvider,
                                          private val cityProvider: CityProvider,
                                          private val displayLanguageProvider: DisplayLanguageProvider,
                                          dateTimeProvider: DateTimeProvider) : CachedRepository(databaseProvider, dateTimeProvider) {

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
                .map { movies -> movies.sortedBy(createSelector()) }
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

    override fun getSyncTime() = arrayOf("05:55", "10:05", "14:05", "19:00", "23:00")

    private fun getMoviesFromApi(): Single<List<Movie>> {
        return apiClient.subsCityService.getMovies(cityProvider.cityId)
                .doOnSuccess { databaseProvider.currentDatabaseClient.movieDao.deleteAllMovies() }
                .doOnSuccess { databaseProvider.currentDatabaseClient.movieDao.saveMovies(it) }
                .doOnSuccess { updateCacheTimestamp() }
                .timeout()
    }

    private fun createSelector(): (Movie) -> String {
        return if (displayLanguageProvider.isRussian) { movie -> movie.title.russian } else { movie -> movie.title.original }
    }

    private fun getMoviesFromDb(): Single<List<Movie>> {
        return databaseProvider.currentDatabaseClient.movieDao.getAllMovies()
                .firstOrError()
    }
}