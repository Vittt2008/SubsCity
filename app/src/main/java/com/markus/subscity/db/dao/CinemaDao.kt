package com.markus.subscity.db.dao

import androidx.room.*
import com.markus.subscity.api.entities.cinema.Cinema
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Vitaliy Markus
 */
@Dao
interface CinemaDao {

    @Query("SELECT * FROM Cinema")
    fun getAllCinemas(): Flowable<List<Cinema>>

    @Query("SELECT * FROM Cinema WHERE id = :id LIMIT 1")
    fun getCinema(id: Long): Single<Cinema>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCinemas(cinemas: List<Cinema>)

    @Query("DELETE FROM Cinema")
    fun deleteAllCinemas()
}