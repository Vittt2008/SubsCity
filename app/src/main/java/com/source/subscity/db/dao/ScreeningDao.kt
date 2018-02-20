package com.source.subscity.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.source.subscity.api.entities.screening.CinemaScreening
import com.source.subscity.api.entities.screening.DateScreening
import com.source.subscity.api.entities.screening.MovieScreening
import io.reactivex.Flowable

/**
 * @author Vitaliy Markus
 */
@Dao
interface ScreeningDao {

    @Query("SELECT * FROM CinemaScreening")
    fun getAllCinemaScreening(): Flowable<List<CinemaScreening>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCinemaScreening(screenings: List<CinemaScreening>)


    @Query("SELECT * FROM DateScreening")
    fun getAllDateScreening(): Flowable<List<DateScreening>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDateScreening(screenings: List<DateScreening>)


    @Query("SELECT * FROM MovieScreening")
    fun getAllMovieScreening(): Flowable<List<MovieScreening>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieScreening(screenings: List<MovieScreening>)


}