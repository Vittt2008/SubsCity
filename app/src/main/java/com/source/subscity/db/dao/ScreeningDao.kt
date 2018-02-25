package com.source.subscity.db.dao

import android.arch.persistence.room.*
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.db.converters.Converter
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

    @Query("SELECT * FROM Screening WHERE movieId = :arg0")
    fun getMovieScreenings(movieId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE cinemaId = :arg0")
    fun getCinemaScreenings(cinemaId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE dateTime > :arg0 AND dateTime < :arg1")
    fun getDateScreenings(from: DateTime, to: DateTime): Flowable<List<Screening>>


}