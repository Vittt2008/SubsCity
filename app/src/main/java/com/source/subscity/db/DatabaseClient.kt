package com.source.subscity.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.CinemaScreening
import com.source.subscity.api.entities.screening.DateScreening
import com.source.subscity.api.entities.screening.MovieScreening
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
    CinemaScreening::class,
    DateScreening::class,
    MovieScreening::class,
    CacheTimestamp::class], version = 1)
abstract class DatabaseClient : RoomDatabase() {

    abstract val cinemaDao: CinemaDao
    abstract val movieDao: MovieDao
    abstract val screeningDao: ScreeningDao
    abstract val cacheTimestampDao: CacheTimestampDao

}