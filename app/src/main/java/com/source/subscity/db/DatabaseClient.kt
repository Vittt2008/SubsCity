package com.source.subscity.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.db.dao.CinemaDao

/**
 * @author Vitaliy Markus
 */
@Database(entities = [Cinema::class], version = 1)
abstract class DatabaseClient : RoomDatabase() {

    abstract val cinemaDao: CinemaDao
}