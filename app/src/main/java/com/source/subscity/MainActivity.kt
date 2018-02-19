package com.source.subscity

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.stetho.Stetho
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.cinema.Location
import com.source.subscity.db.DatabaseClient
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)

        val database = Room.databaseBuilder(this, DatabaseClient::class.java, "subs_city").build()
        val cinemaDao = database.cinemaDao
        val cinemas = getCinemas()

        val list2 = Completable.fromAction { cinemaDao.saveCinemas(cinemas) }
                .andThen(cinemaDao.getAllCinemas())
                .subscribeOn(Schedulers.io())
                .map { cinemas == it }
                .subscribe({ res ->
                    val result = res and true
                }, { error ->
                    val str = error.toString()
                })
    }

    private fun getCinemas(): List<Cinema> {
        val cinema1 = Cinema(
                1,
                Location("1", listOf("1", "11", "111"), 1.1, 11.11),
                listOf(1, 11, 111),
                3,
                "1,11,111",
                listOf("1", "11", "111"),
                listOf("1", "11", "111")
        )
        val cinema2 = Cinema(
                2,
                Location("2", listOf("2", "22", "222"), 2.2, 22.22),
                listOf(2, 22, 222),
                3,
                "2,22,222",
                listOf("2", "22", "222"),
                listOf("2", "22", "222")
        )
        val cinema3 = Cinema(
                3,
                Location("3", listOf("3", "33", "333"), 3.3, 33.33),
                listOf(3, 33, 333),
                3,
                "3,33,333",
                listOf("3", "33", "333"),
                listOf("3", "33", "333")
        )
        val cinema4 = Cinema(
                4,
                Location("4", listOf("4", "44", "444"), 4.4, 44.44),
                listOf(4, 44, 444),
                3,
                "4,44,444",
                listOf("4", "44", "444"),
                listOf("4", "44", "444")
        )
        return listOf(cinema1, cinema2, cinema3, cinema4)
    }

}
