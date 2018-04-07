package com.markus.subscity.db.dao

import android.arch.persistence.room.*
import com.markus.subscity.api.entities.movie.Movie
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Vitaliy Markus
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flowable<List<Movie>>

    @Query("SELECT * FROM Movie WHERE id = :id LIMIT 1")
    fun getMovie(id: Long): Single<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(movies: List<Movie>)

    @Query("DELETE FROM Movie")
    fun deleteAllMovies()
}