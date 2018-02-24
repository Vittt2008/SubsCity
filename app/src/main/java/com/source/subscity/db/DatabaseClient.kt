package com.source.subscity.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.db.dao.CacheTimestampDao
import com.source.subscity.db.dao.CinemaDao
import com.source.subscity.db.dao.MovieDao
import com.source.subscity.db.dao.ScreeningDao
import com.source.subscity.db.entity.CacheTimestamp

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