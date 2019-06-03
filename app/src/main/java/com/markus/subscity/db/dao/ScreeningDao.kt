package com.markus.subscity.db.dao

import androidx.room.*
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.db.converters.Converter
import io.reactivex.Flowable
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
@Dao
@TypeConverters(Converter::class)
interface ScreeningDao {

    @Query("SELECT * FROM Screening")
    fun getAllScreenings(): Flowable<List<Screening>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveScreening(screenings: List<Screening>)

    @Query("SELECT * FROM Screening WHERE movieId = :movieId")
    fun getMovieScreenings(movieId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE cinemaId = :cinemaId")
    fun getCinemaScreenings(cinemaId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE dateTime > :from AND dateTime < :to")
    fun getDateScreenings(from: DateTime, to: DateTime): Flowable<List<Screening>>


}