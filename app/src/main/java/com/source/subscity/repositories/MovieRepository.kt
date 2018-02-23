package com.source.subscity.repositories

import com.source.subscity.api.ApiClient
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.db.DatabaseClient
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieRepository @Inject constructor(apiClient: ApiClient, databaseClient: DatabaseClient) :
        CachedRepository(apiClient, databaseClient) {

    private val city = "spb"

    fun getMovies(): Single<List<Movie>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getMoviesFromDb()
                    } else {
                        getMoviesFromApi()
                                .onErrorResumeNext(getMoviesFromDb())
                    }
                }
                .map { movies -> movies.sortedBy { it.title.russian } }
    }

    fun getMovie(id: Long): Single<Movie> {
        return databaseClient.movieDao.getMovie(id)
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
        return apiClient.subsCityService.getMovies(city)
                .doOnSuccess { it -> databaseClient.movieDao.saveMovies(it) }
                .doOnSuccess { updateCacheTimestamp() }
    }

    private fun getMoviesFromDb(): Single<List<Movie>> {
        return databaseClient.movieDao.getAllMovies()
                .firstOrError()
    }
}