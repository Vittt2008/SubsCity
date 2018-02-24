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
    fun getAllScreening(): Flowable<List<Screening>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveScreening(screenings: List<Screening>)

    @Query("SELECT * FROM Screening WHERE movieId = :arg0")
    fun getScreeningByMovie(movieId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE cinemaId = :arg0")
    fun getScreeningByCinema(cinemaId: Long): Flowable<List<Screening>>

    @Query("SELECT * FROM Screening WHERE dateTime = :arg0")
    fun getScreeningByDate(date: DateTime): Flowable<List<Screening>>


}