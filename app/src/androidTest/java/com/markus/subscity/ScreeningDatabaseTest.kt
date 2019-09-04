package com.markus.subscity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.reflect.TypeToken
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.db.DatabaseClient
import com.markus.subscity.db.dao.ScreeningDao
import com.markus.subscity.helper.createGson
import io.reactivex.Completable
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4::class)
class ScreeningDatabaseTest {

    private val allScreenings: List<Screening> by lazy {
        val listScreeningType = object : TypeToken<List<Screening>>() {}.type
        createGson().fromJson<List<Screening>>(screeningJson, listScreeningType)
    }
    private val screeningDao: ScreeningDao by lazy {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Room.databaseBuilder(appContext, DatabaseClient::class.java, "subs_city")
                .setQueryExecutor { runnable -> runnable.run() }
                .build().screeningDao
    }

    @Test
    fun checkSaveAndGetScreening() {
        val testObserver = Completable.fromAction { screeningDao.saveScreening(allScreenings) }
                .andThen(screeningDao.getAllScreenings())
                .map { screening -> allScreenings.sortedBy { it.id } == screening.sortedBy { it.id } }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndGetMovieId() {
        val expectedScreening = allScreenings.random()
        val expectedScreenings = allScreenings.filter { it.movieId == expectedScreening.movieId }.sortedBy { it.id }
        val testObserver = Completable.fromAction { screeningDao.saveScreening(allScreenings) }
                .andThen(screeningDao.getMovieScreenings(expectedScreening.movieId))
                .map { screening -> screening.sortedBy { it.id } }
                .map { screenings -> screenings == expectedScreenings }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndGetCinemaId() {
        val expectedScreening = allScreenings.random()
        val expectedScreenings = allScreenings.filter { it.cinemaId == expectedScreening.cinemaId }.sortedBy { it.id }
        val testObserver = Completable.fromAction { screeningDao.saveScreening(allScreenings) }
                .andThen(screeningDao.getCinemaScreenings(expectedScreening.cinemaId))
                .map { screening -> screening.sortedBy { it.id } }
                .map { screenings -> screenings == expectedScreenings }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    companion object {
        private val screeningJson = """
[  
   {  
      "cinema_id":81,
      "date_time":"2019-09-06T14:35:00+03:00",
      "id":271361,
      "screening_id":70291,
      "price_max":190,
      "price_min":190,
      "tickets_url":"https://subscity.ru/screenings/tickets/271361"
   },
   {  
      "cinema_id":83,
      "date_time":"2019-09-06T16:05:00+03:00",
      "id":271106,
      "screening_id":70291,
      "price_max":240,
      "price_min":240,
      "tickets_url":"https://subscity.ru/screenings/tickets/271106"
   },
   {  
      "cinema_id":65,
      "date_time":"2019-09-06T19:05:00+03:00",
      "id":271644,
      "screening_id":70318,
      "price_max":390,
      "price_min":390,
      "tickets_url":"https://subscity.ru/screenings/tickets/271644"
   },
   {  
      "cinema_id":121,
      "date_time":"2019-09-06T19:50:00+03:00",
      "id":271409,
      "screening_id":70414,
      "price_max":300,
      "price_min":300,
      "tickets_url":"https://subscity.ru/screenings/tickets/271409"
   },
   {  
      "cinema_id":70,
      "date_time":"2019-09-06T19:55:00+03:00",
      "id":271616,
      "screening_id":70291,
      "price_max":390,
      "price_min":390,
      "tickets_url":"https://subscity.ru/screenings/tickets/271616"
   },
   {  
      "cinema_id":65,
      "date_time":"2019-09-06T20:00:00+03:00",
      "id":270903,
      "screening_id":70291,
      "price_max":440,
      "price_min":440,
      "tickets_url":"https://subscity.ru/screenings/tickets/270903"
   },
   {  
      "cinema_id":121,
      "date_time":"2019-09-06T20:35:00+03:00",
      "id":271360,
      "screening_id":70291,
      "price_max":300,
      "price_min":300,
      "tickets_url":"https://subscity.ru/screenings/tickets/271360"
   },
   {  
      "cinema_id":81,
      "date_time":"2019-09-06T21:15:00+03:00",
      "id":271362,
      "screening_id":70291,
      "price_max":280,
      "price_min":280,
      "tickets_url":"https://subscity.ru/screenings/tickets/271362"
   },
   {  
      "cinema_id":121,
      "date_time":"2019-09-06T21:20:00+03:00",
      "id":270943,
      "screening_id":70412,
      "price_max":null,
      "price_min":null,
      "tickets_url":"https://subscity.ru/screenings/tickets/270943"
   },
   {  
      "cinema_id":121,
      "date_time":"2019-09-06T21:30:00+03:00",
      "id":271392,
      "screening_id":70412,
      "price_max":300,
      "price_min":300,
      "tickets_url":"https://subscity.ru/screenings/tickets/271392"
   },
   {  
      "cinema_id":65,
      "date_time":"2019-09-06T23:25:00+03:00",
      "id":271615,
      "screening_id":70291,
      "price_max":440,
      "price_min":440,
      "tickets_url":"https://subscity.ru/screenings/tickets/271615"
   }
]    """.trimIndent()
    }
}
