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

    fun getMovies(city: String): Single<List<Movie>> {
        return isCacheActual()
                .flatMap { isActual ->
                    if (isActual) {
                        getMoviesFromDb()
                    } else {
                        apiClient.subsCityService.getMovies(city)
                                .doOnSuccess { it -> databaseClient.movieDao.saveMovies(it) }
                                .doOnSuccess { updateCacheTimestamp() }
                                .onErrorResumeNext(getMoviesFromDb())
                    }
                }
                .map { movies -> movies.sortedBy { it.title.russian } }
    }

    fun getMovie(id: String): Single<Movie> {
        return databaseClient.movieDao.getMovie(id)
    }

    override fun getDefaultCacheKey() = "movie"

    override fun getCacheLifetime() = TimeUnit.DAYS.toMillis(1)

    private fun getMoviesFromDb(): Single<List<Movie>> {
        return databaseClient.movieDao.getAllMovies()
                .firstOrError()
    }
}