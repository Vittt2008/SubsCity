package com.source.subscity.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.source.subscity.api.entities.cinema.Cinema
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Vitaliy Markus
 */
@Dao
interface CinemaDao {

    @Query("SELECT * FROM Cinema")
    fun getAllCinemas(): Flowable<List<Cinema>>

    @Query("SELECT * FROM Cinema WHERE id = :arg0 LIMIT 1")
    fun getCinema(id: Long): Single<Cinema>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCinemas(cinemas: List<Cinema>)
}