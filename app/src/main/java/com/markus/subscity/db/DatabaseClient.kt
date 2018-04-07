package com.markus.subscity.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.db.dao.CacheTimestampDao
import com.markus.subscity.db.dao.CinemaDao
import com.markus.subscity.db.dao.MovieDao
import com.markus.subscity.db.dao.ScreeningDao
import com.markus.subscity.db.entity.CacheTimestamp

/**
 * @author Vitaliy Markus
 */
@Database(entities = [
    Cinema::class,
    Movie::class,
    Screening::class,
    CacheTimestamp::class], version = 1)
abstract class DatabaseClient : RoomDatabase() {

    abstract val cinemaDao: CinemaDao
    abstract val movieDao: MovieDao
    abstract val screeningDao: ScreeningDao
    abstract val cacheTimestampDao: CacheTimestampDao

}