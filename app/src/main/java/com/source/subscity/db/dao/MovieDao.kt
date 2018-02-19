package com.source.subscity.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.source.subscity.api.entities.movie.Movie
import io.reactivex.Flowable

/**
 * @author Vitaliy Markus
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM Cinema")
    fun getAllMovies(): Flowable<List<Movie>>
}