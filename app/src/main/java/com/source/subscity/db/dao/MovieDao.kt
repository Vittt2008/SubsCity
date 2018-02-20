package com.source.subscity.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.source.subscity.api.entities.movie.Movie
import io.reactivex.Flowable

/**
 * @author Vitaliy Markus
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flowable<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(movies: List<Movie>)
}