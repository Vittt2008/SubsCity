package com.markus.subscity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.reflect.TypeToken
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.db.DatabaseClient
import com.markus.subscity.db.dao.CinemaDao
import com.markus.subscity.helper.createGson
import io.reactivex.Completable
import junit.framework.Assert.assertTrue
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4::class)
class CinemaDatabaseTest {

    private val expectedCinemas: List<Cinema> by lazy {
        val listCinemaType = object : TypeToken<List<Cinema>>() {}.type
        createGson().fromJson<List<Cinema>>(cinemasJson, listCinemaType)
    }
    private val cinemaDao: CinemaDao by lazy {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Room.databaseBuilder(appContext, DatabaseClient::class.java, "subs_city")
                .setQueryExecutor { runnable -> runnable.run() }
                .build().cinemaDao
    }

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        JodaTimeAndroid.init(appContext);
    }

    @Test
    fun checkSaveAndGetCinemas() {
        val testObserver = Completable.fromAction { cinemaDao.saveCinemas(expectedCinemas) }
                .andThen(cinemaDao.getAllCinemas())
                .map { cinemas -> expectedCinemas.sortedBy { it.id } == cinemas.sortedBy { it.id } }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndGetById() {
        val expectedCinema = expectedCinemas.random()
        val testObserver = Completable.fromAction { cinemaDao.saveCinemas(expectedCinemas) }
                .andThen(cinemaDao.getCinema(expectedCinema.id))
                .map { cinema -> cinema == expectedCinema }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndRemoveAll() {
        val testObserver = Completable.fromAction { cinemaDao.saveCinemas(expectedCinemas) }
                .andThen(Completable.fromAction { cinemaDao.deleteAllCinemas() })
                .andThen(cinemaDao.getAllCinemas())
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first().isEmpty())
    }

    companion object {
        private val cinemasJson = """
[  
   {  
      "id":87,
      "location":{  
         "address":"М.Морская, 24, отель «Англетер»",
         "metro":[  
            "Адмиралтейская"
         ],
         "latitude":59.933916,
         "longitude":30.308584
      },
      "movies":[  
         70474,
         70453,
         69987,
         70104,
         70412,
         70364,
         70318,
         70291,
         70454,
         70227
      ],
      "movies_count":10,
      "name":"Angleterre Cinema Lounge",
      "phones":[  
         "+7 (812) 494 59 90",
         "+7 (812) 494 50 63"
      ],
      "urls":[  
         "http://www.angleterrecinema.ru"
      ]
   },
   {  
      "id":229,
      "location":{  
         "address":"Потемкинская, 4",
         "metro":[  
            "Чернышевская"
         ],
         "latitude":59.945834,
         "longitude":30.368194
      },
      "movies":[  
         69748
      ],
      "movies_count":1,
      "name":"Ленинград-центр",
      "phones":[  
         "+7 (812) 242 9999"
      ],
      "urls":[  
         "http://leningradcenter.ru/"
      ]
   },
   {  
      "id":125,
      "location":{  
         "address":"Каменноостровский просп., 10",
         "metro":[  
            "Горьковская"
         ],
         "latitude":59.958301,
         "longitude":30.316858
      },
      "movies":[  
         70454
      ],
      "movies_count":1,
      "name":"Ленфильм",
      "phones":[  
         "+7 (911) 991-25-02"
      ],
      "urls":[  
         "http://www.lenfilm.ru/service/Kinoteatr-Lenfilm"
      ]
   },
   {  
      "id":81,
      "location":{  
         "address":"Индустриальный просп., 24, литера А, ТРЦ «Июнь»",
         "metro":[  
            "Ладожская"
         ],
         "latitude":59.946188,
         "longitude":30.475826
      },
      "movies":[  
         70291,
         70478
      ],
      "movies_count":2,
      "name":"Мори Синема",
      "phones":[  
         "+7 800 555-59-99"
      ],
      "urls":[  
         "http://mori-cinema.ru"
      ]
   },
   {  
      "id":83,
      "location":{  
         "address":"Сенная пл., 2, «Пик»",
         "metro":[  
            "Сенная площадь",
            "Садовая"
         ],
         "latitude":59.926461,
         "longitude":30.320629
      },
      "movies":[  
         70291
      ],
      "movies_count":1,
      "name":"Пик",
      "phones":[  
         "+7 812 449-24-33"
      ],
      "urls":[  
         "http://kinopik.info/"
      ]
   },
   {  
      "id":121,
      "location":{  
         "address":"Караванная, 12",
         "metro":[  
            "Невский проспект",
            "Гостиный Двор"
         ],
         "latitude":59.936254,
         "longitude":30.34175
      },
      "movies":[  
         70242,
         69987,
         70412,
         70291,
         70267,
         70414
      ],
      "movies_count":6,
      "name":"Родина",
      "phones":[  
         "+7 (812) 314-28-27",
         "+7 (812) 571-61-31"
      ],
      "urls":[  
         "http://rodinakino.ru/"
      ]
   },
   {  
      "id":70,
      "location":{  
         "address":"просп. Космонавтов, 14, «Питер-Радуга»",
         "metro":[  
            "Парк Победы",
            "Московская"
         ],
         "latitude":59.866695,
         "longitude":30.351995
      },
      "movies":[  
         70291
      ],
      "movies_count":1,
      "name":"Синема Парк Радуга",
      "phones":[  
         "+7 800 700-01-11"
      ],
      "urls":[  
         "http://www.cinemapark.ru/multiplexes/show/23/"
      ]
   },
   {  
      "id":65,
      "location":{  
         "address":"Лиговский просп., 30а, «Галерея»",
         "metro":[  
            "Площадь Восстания"
         ],
         "latitude":59.928346,
         "longitude":30.360503
      },
      "movies":[  
         70318,
         70291
      ],
      "movies_count":2,
      "name":"Формула Кино Галерея",
      "phones":[  
         "+7 800 250 80 25"
      ],
      "urls":[  
         "http://www.formulakino.ru"
      ]
   }
]
    """.trimIndent()
    }
}
