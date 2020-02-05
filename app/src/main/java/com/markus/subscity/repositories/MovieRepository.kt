package com.markus.subscity.repositories

import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.providers.*
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.rx2.asFlowable
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.rx2.rxSingle
import java.lang.Exception
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
                        rxSingle { getMoviesFromApi() }
                                .onErrorResumeNext { getMoviesFromDb() }
                    }
                }
                .map { movies -> movies.sortedBy(createSelector()) }
    }

    suspend fun getMoviesSuspend(): List<Movie> {
        val isActual = isCacheActual().await()
        return if (isActual) {
            getMoviesFromDb().await()
        } else {
            try {
                getMoviesFromApi()
            } catch (e: Exception) {
                getMoviesFromDb().await()
            }
        }.sortedBy(createSelector())
    }

    fun getMovie(id: Long): Single<Movie> {
        return databaseProvider.currentDatabaseClient.movieDao.getMovie(id)
                .onErrorResumeNext {
                    rxSingle {
                        val movies = getMoviesFromApi()
                        movies.first { it.id == id }
                    }
                }
    }

    override fun getDefaultCacheKey() = "movie"

    override fun getSyncTime() = arrayOf("05:55", "10:05", "14:05", "19:00", "23:00")

    private suspend fun getMoviesFromApi(): List<Movie> {
        val movies = apiClient.subsCityService.getMovies(cityProvider.cityId)
        databaseProvider.currentDatabaseClient.movieDao.deleteAllMovies()
        databaseProvider.currentDatabaseClient.movieDao.saveMovies(movies)
        updateCacheTimestamp()
        return movies
    }

    private fun createSelector(): (Movie) -> String {
        return if (displayLanguageProvider.isRussian) { movie -> movie.title.russian } else { movie -> movie.title.original }
    }

    private fun getMoviesFromDb(): Single<List<Movie>> {
        return databaseProvider.currentDatabaseClient.movieDao.getAllMovies()
                .firstOrError()
    }
}