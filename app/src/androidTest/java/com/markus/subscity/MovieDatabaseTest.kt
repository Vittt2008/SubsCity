package com.markus.subscity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.reflect.TypeToken
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.db.DatabaseClient
import com.markus.subscity.db.dao.MovieDao
import com.markus.subscity.helper.createGson
import io.reactivex.Completable
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {

    private val expectedMovies: List<Movie> by lazy {
        val listMovieType = object : TypeToken<List<Movie>>() {}.type
        createGson().fromJson(moviesJson, listMovieType)
    }
    private val movieDao: MovieDao by lazy {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Room.databaseBuilder(appContext, DatabaseClient::class.java, "subs_city")
                .setQueryExecutor { runnable -> runnable.run() }
                .build().movieDao
    }

    @Test
    fun checkSaveAndGetMovies() {
        val testObserver = Completable.fromAction { movieDao.saveMovies(expectedMovies) }
                .andThen(movieDao.getAllMovies())
                .map { movies -> expectedMovies.sortedBy { it.id } == movies.sortedBy { it.id } }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndGetById() {
        val expectedMovie = expectedMovies.random()
        val testObserver = Completable.fromAction { movieDao.saveMovies(expectedMovies) }
                .andThen(movieDao.getMovie(expectedMovie.id))
                .map { movie -> movie == expectedMovie }
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first())
    }

    @Test
    fun checkSaveAndRemoveAll() {
        val testObserver = Completable.fromAction { movieDao.saveMovies(expectedMovies) }
                .andThen(Completable.fromAction { movieDao.deleteAllMovies() })
                .andThen(movieDao.getAllMovies())
                .test()
        assertTrue(testObserver.values().size == 1)
        assertTrue(testObserver.values().first().isEmpty())
    }

    companion object {
        private val moviesJson = """
[  
   {  
      "age_restriction":18,
      "cast":[  
         "Антонелла Аквистапаче",
         "Фабиан Аренильяс",
         "Ромина Бентанкур",
         "Валерия Лоис",
         "Федерико Морозини"
      ],
      "countries":[  
         "Уругвай",
         "Аргентина",
         "Испания"
      ],
      "created_at":"2019-08-22T19:15:58+03:00",
      "description":"В то время как сообщения об акулах вызывают панику в небольшом пляжном городке, 14-летняя Розина начинает кружить вокруг своей собственной добычи, поскольку ее сильное влечение к молодому человеку старше ее растёт с каждым днём.\r\n",
      "directors":[  
         "Лусия Гарибальди"
      ],
      "duration":80,
      "genres":[  
         "драма"
      ],
      "id":70474,
      "languages":[  
         "испанский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70474.jpg?1566642947",
      "rating":{  
         "imdb":{  
            "id":9358136,
            "rating":6.9,
            "votes":87
         },
         "kinopoisk":{  
            "id":1220074,
            "rating":null,
            "votes":33
         }
      },
      "screenings":{  
         "count":2,
         "next":"2019-09-05T18:00:00+03:00"
      },
      "title":{  
         "original":"Los tiburones",
         "russian":"Акулы"
      },
      "trailer":{  
         "original":"Wf3KuDWe3VA",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":18,
      "cast":[  
         "Ким Хе Су",
         "Ю А Ин",
         "Хо Чжун Хо",
         "Венсан Кассель"
      ],
      "countries":[  
         "Южная Корея"
      ],
      "created_at":"2019-08-21T19:00:38+03:00",
      "description":"В 1997 году южнокорейское правительство было уверено, что экономика на подъеме и страну ждет стабильное процветание. Но по расчетам главы отдела по денежно-кредитной политике Банка Кореи в течение недели страна столкнется с финансовым кризисом. Женщина предлагает проинформировать население, но есть те, в чьих интересах умолчать о грядущем дефолте и нажиться на ситуации.",
      "directors":[  
         "Чхве Гук Хи"
      ],
      "duration":114,
      "genres":[  
         "драма"
      ],
      "id":70453,
      "languages":[  
         "корейский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70453.jpg?1567538278",
      "rating":{  
         "imdb":{  
            "id":7233726,
            "rating":6.7,
            "votes":559
         },
         "kinopoisk":{  
            "id":1047328,
            "rating":null,
            "votes":84
         }
      },
      "screenings":{  
         "count":2,
         "next":"2019-09-05T15:50:00+03:00"
      },
      "title":{  
         "original":"Gukgabudo-ui Nal",
         "russian":"Дефолт"
      },
      "trailer":{  
         "original":"iw_wt2hHW2w",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":[  
         "Тиль Швайгер",
         "Ян Йозеф Лиферс",
         "Мориц Бляйбтрой",
         "Тьерри ван Вервеке"
      ],
      "countries":[  
         "Германия",
         "Бельгия"
      ],
      "created_at":"2019-08-06T19:15:08+03:00",
      "description":"Судьба сводит героев картины в больнице, где врачи выносят им смертный приговор. Счет времени их жизней идет на часы. Дальнейшие события в фильме разворачиваются в стремительном темпе. Украв машину с миллионом немецких марок в багажнике, они сбегают из больницы.\r\n\r\nИх преследуют наемные убийцы, они становятся грабителями поневоле, за ними гонится полиция, они попадают в бордель. Но тем не менее продолжают мчаться вперед, навстречу своей судьбе.",
      "directors":[  
         "Томас Ян"
      ],
      "duration":87,
      "genres":[  
         "боевик",
         "трагикомедия",
         "криминальный"
      ],
      "id":70242,
      "languages":[  
         "немецкий"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70242.jpg?1567538520",
      "rating":{  
         "imdb":{  
            "id":119472,
            "rating":8,
            "votes":25320
         },
         "kinopoisk":{  
            "id":32898,
            "rating":8.6,
            "votes":364678
         }
      },
      "screenings":{  
         "count":2,
         "next":"2019-09-07T19:00:00+03:00"
      },
      "title":{  
         "original":"Knockin' on Heaven's Door",
         "russian":"Достучаться до небес"
      },
      "trailer":{  
         "original":null,
         "russian":null
      },
      "year":1997
   },
   {  
      "age_restriction":12,
      "cast":[  
         "Лили Джеймс",
         "Джессика Браун Финдли",
         "Глен Пауэлл",
         "Мэттью Гуд"
      ],
      "countries":[  
         "Великобритания",
         "Франция",
         "США"
      ],
      "created_at":"2019-07-16T19:01:56+03:00",
      "description":"В послевоенном Лондоне молодая писательница Джулиет пытается найти сюжет для новой книги, но об ужасах войны писать ей решительно не хочется, прочие темы кажутся либо скучными, либо неуместными. На помощь приходит случай — в виде письма одного свиновода с острова Гернси. Оказывается, даже свинари любят почитать, и неведомый Доуси, к которому в руки попала книга, некогда принадлежавшая Джулиет, просит ее посоветовать хорошую книжную лавку. Дело в том, что на Гернси с книгами сейчас туго, поскольку остров, в годы войны оккупированный немцами, только-только возрождается к жизни. Письмо переворачивает жизнь Джулиет. История книжного клуба, ставшего прикрытием для запрещенных встреч жителей деревни, увлекает и затягивает ее. Так начинается переписка с самыми разными людьми.",
      "directors":[  
         "Майк Ньюэлл"
      ],
      "duration":124,
      "genres":[  
         "исторический",
         "мелодрама"
      ],
      "id":69987,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/69987.jpg?1566133317",
      "rating":{  
         "imdb":{  
            "id":1289403,
            "rating":7.4,
            "votes":24208
         },
         "kinopoisk":{  
            "id":467760,
            "rating":7.3,
            "votes":4692
         }
      },
      "screenings":{  
         "count":2,
         "next":"2019-09-07T12:25:00+03:00"
      },
      "title":{  
         "original":"The Guernsey Literary and Potato Peel Pie Society",
         "russian":"Клуб любителей книг и пирогов из картофельных очистков"
      },
      "trailer":{  
         "original":"vP9eDmX0ow0",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":18,
      "cast":[  
         "Петер Белли",
         "Лейф Эдлунд",
         "Ильва Галлон",
         "Катарина Якобсон"
      ],
      "countries":[  
         "Швеция",
         "Дания"
      ],
      "created_at":"2019-07-23T23:02:05+03:00",
      "description":"Семейная пара отправляется в путешествие в надежде восстановить свои взаимоотношения. В лесу из тени появляются странноватые артисты цирка, которые принимаются терроризировать супругов, вовлекая их все глубже в пучину психологического ужаса и унизительных издевательств.",
      "directors":[  
         "Йоханнес Нюхольм"
      ],
      "duration":86,
      "genres":[  
         "драма"
      ],
      "id":70104,
      "languages":[  
         "шведский",
         "датский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70104.jpg?1566987279",
      "rating":{  
         "imdb":{  
            "id":9355200,
            "rating":6.6,
            "votes":296
         },
         "kinopoisk":{  
            "id":1221268,
            "rating":6.5,
            "votes":194
         }
      },
      "screenings":{  
         "count":1,
         "next":"2019-09-08T15:10:00+03:00"
      },
      "title":{  
         "original":"Koko-di Koko-da",
         "russian":"Коко-ди коко-да"
      },
      "trailer":{  
         "original":"eqMEuLgPIpU",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":[  
         "Кейт Бланшетт",
         "Кристен Уиг",
         "Джуди Грир",
         "Лоренс Фишберн"
      ],
      "countries":[  
         "США"
      ],
      "created_at":"2019-08-19T23:02:28+03:00",
      "description":"У Бернадетт, очаровательной женщины и талантливого архитектора, есть все: прекрасный дом, замечательная дочь, успешный и любящий муж. Хоть она и выбивается из «безупречной» компании соседских мамаш, никто не мог представить, что в один прекрасный день Бернадетт просто исчезнет без следа. Она отправляется на поиски себя, пытаясь обрести счастье на краю света.",
      "directors":[  
         "Ричард Линклейтер"
      ],
      "duration":130,
      "genres":[  
         "детектив",
         "комедия"
      ],
      "id":70412,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70412.jpg?1567276674",
      "rating":{  
         "imdb":{  
            "id":2365580,
            "rating":6.6,
            "votes":1552
         },
         "kinopoisk":{  
            "id":702904,
            "rating":null,
            "votes":51
         }
      },
      "screenings":{  
         "count":13,
         "next":"2019-09-05T13:40:00+03:00"
      },
      "title":{  
         "original":"Where'd You Go, Bernadette",
         "russian":"Куда ты пропала, Бернадетт?"
      },
      "trailer":{  
         "original":"pqnroADyAqQ",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":[  
         "Франсуа Клюзе",
         "Марион Котийяр",
         "Жиль Лелуш",
         "Лоран Лафитт"
      ],
      "countries":[  
         "Франция",
         "Бельгия"
      ],
      "created_at":"2019-08-16T23:02:33+03:00",
      "description":"Веселая компания готовит сюрприз для своего закадычного друга Макса Кантара (Франсуа Клюзе) и без предупреждения заваливается к нему на виллу на мысе Кап-Ферре, чтобы поздравить с юбилеем... Но, судя по всему, у Макса нет особого повода для веселья. \r\n\r\nВ «Маленьких секретах» комедия и драма идут рука об руку. Здесь шутят над серьезными вещами, кажущимися порой печальными. Прощают, любят, радуются и остаются собой, несмотря на осуждение окружающих. \r\n\r\nЭтот фильм о том, что нам не обойтись без друзей и их поддержки, без их любви и понимания, готовности подставить плечо в трудную минуту.",
      "directors":[  
         "Гийом Кане"
      ],
      "duration":135,
      "genres":[  
         "трагикомедия"
      ],
      "id":70364,
      "languages":[  
         "французский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70364.jpg?1566642826",
      "rating":{  
         "imdb":{  
            "id":8201404,
            "rating":6.6,
            "votes":720
         },
         "kinopoisk":{  
            "id":1127012,
            "rating":7.4,
            "votes":674
         }
      },
      "screenings":{  
         "count":1,
         "next":"2019-09-08T23:00:00+03:00"
      },
      "title":{  
         "original":"Nous finirons ensemble",
         "russian":"Маленькие секреты большой компании"
      },
      "trailer":{  
         "original":"RjfF0V5blEE",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":18,
      "cast":[  
         "Дакота Фаннинг",
         "Курт Рассел",
         "Брэд Питт",
         "Леонардо ДиКаприо"
      ],
      "countries":[  
         "Великобритания",
         "США"
      ],
      "created_at":"2019-08-12T23:00:21+03:00",
      "description":"9-й фильм оскароносного режиссера Квентина Тарантино, повествующий о череде событий, произошедших в Голливуде в 1969 году, на закате его «золотого века». По сюжету, известный ТВ актер Рик Далтон (Леонардо ДиКаприо) и его дублер Клифф Бут (Брэд Питт) пытаются найти свое место в стремительно меняющемся мире киноиндустрии.",
      "directors":[  
         "Квентин Тарантино"
      ],
      "duration":168,
      "genres":[  
         "трагикомедия"
      ],
      "id":70318,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70318.jpg?1566133833",
      "rating":{  
         "imdb":{  
            "id":7131622,
            "rating":8,
            "votes":164342
         },
         "kinopoisk":{  
            "id":1047883,
            "rating":7.7,
            "votes":76409
         }
      },
      "screenings":{  
         "count":8,
         "next":"2019-09-05T19:05:00+03:00"
      },
      "title":{  
         "original":"Once Upon a Time... in Hollywood",
         "russian":"Однажды в... Голливуде"
      },
      "trailer":{  
         "original":"ELeMaP8EPAA",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":18,
      "cast":[  
         "Билл Скарсгорд",
         "Джессика Честейн",
         "Джеймс МакЭвой",
         "София Лиллис"
      ],
      "countries":[  
         "Андрес Мускьетти"
      ],
      "created_at":"2019-08-10T05:56:24+03:00",
      "description":"Проходит 27 лет после первой встречи ребят с демоническим Пеннивайзом. Они уже выросли, и у каждого своя жизнь. Но неожиданно их спокойное существование нарушает странный телефонный звонок, который заставляет их вновь собраться вместе.",
      "directors":[  
         "Андрес Мускьетти"
      ],
      "duration":169,
      "genres":[  
         "ужасы"
      ],
      "id":70291,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70291.jpg?1567276575",
      "rating":{  
         "imdb":{  
            "id":7349950,
            "rating":7.9,
            "votes":934
         },
         "kinopoisk":{  
            "id":686898,
            "rating":null,
            "votes":98
         }
      },
      "screenings":{  
         "count":49,
         "next":"2019-09-05T14:35:00+03:00"
      },
      "title":{  
         "original":"It Chapter Two",
         "russian":"Оно-2"
      },
      "trailer":{  
         "original":"xhJ5P7Up3jA",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":[  
         "Лучано Паваротти",
         "Пласидо Доминго",
         "Спайк Ли",
         "Фил Донахью",
         "Стиви Уандер"
      ],
      "countries":[  
         "Великобритания",
         "США"
      ],
      "created_at":"2019-07-06T05:55:59+03:00",
      "description":"Проделав долгий путь от сына простого пекаря до лучшего тенора в истории, он собирал до 500 тысяч зрителей и дважды попадал в книгу рекордов Гиннеса. Лучано Паваротти по сей день символизирует недосягаемую высоту не только в искусстве, но и в стремлении сделать жизни людей по всему миру лучше.",
      "directors":[  
         "Рон Ховард"
      ],
      "duration":114,
      "genres":[  
         "биография",
         "документальный",
         "музыкальный"
      ],
      "id":69748,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/69748.jpg?1564081004",
      "rating":{  
         "imdb":{  
            "id":6964076,
            "rating":7.6,
            "votes":549
         },
         "kinopoisk":{  
            "id":1064748,
            "rating":8,
            "votes":183
         }
      },
      "screenings":{  
         "count":1,
         "next":"2019-09-07T18:00:00+03:00"
      },
      "title":{  
         "original":"Pavarotti",
         "russian":"Паваротти"
      },
      "trailer":{  
         "original":"xjGPHd0iU60",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":18,
      "cast":[  
         "Том Шиллинг",
         "Себастьян Кох",
         "Паула Бир",
         "Саския Розендаль"
      ],
      "countries":[  
         "Германия",
         "Италия"
      ],
      "created_at":"2019-08-21T23:01:38+03:00",
      "description":"Молодой художник Курт Барнерт (Том Шиллинг) сбегает из Восточной Германии в Западную, чтобы учиться живописи и свободно работать. Но прошлое настигает его, когда в отце своей возлюбленной он узнает человека, ответственного за страшные преступления. Курт разоблачит его через свои картины, которые со временем станут манифестом целого поколения.\r\n\r\nОсновано на фактах биографии немецкого художника Герхарда Рихтера.",
      "directors":[  
         "Флориан Хенкель фон Доннерсмарк"
      ],
      "duration":188,
      "genres":[  
         "исторический",
         "триллер",
         "драма"
      ],
      "id":70454,
      "languages":[  
         "немецкий"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70454.jpg?1567538355",
      "rating":{  
         "imdb":{  
            "id":5311542,
            "rating":7.7,
            "votes":7288
         },
         "kinopoisk":{  
            "id":959606,
            "rating":7.5,
            "votes":1195
         }
      },
      "screenings":{  
         "count":9,
         "next":"2019-09-05T10:15:00+03:00"
      },
      "title":{  
         "original":"Werk ohne Autor",
         "russian":"Работа без авторства"
      },
      "trailer":{  
         "original":"vfUK_adu26g",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":null,
      "countries":[  
         "Великобритания"
      ],
      "created_at":"2019-08-08T19:00:44+03:00",
      "description":"Это исследование о художниках-визионерах, от фламандских мастеров эпохи Возрождения до авангардного движения сюрреалистов и непризнанных гениев ар-брют, или искусства аутсайдеров (Outsider Art). Фильм раскрывает историю этого направления, зарождавшегося в темных уголках викторианских психиатрических лечебниц и представленного сейчас в лучших галереях, музеях и частных коллекциях мира. Авторы документальной картины исследуют связь между искусством и безумием, беседуя с искусствоведами, художниками, кураторами музеев, психиатрами и нейробиологами. Тема безумия вдохновляла самых необычных художников в истории, но и душевнобольные люди тоже часто испытывали непреодолимое желание творить.",
      "directors":[  
         "Амели Равалек"
      ],
      "duration":72,
      "genres":[  
         "документальный"
      ],
      "id":70267,
      "languages":[  
         "английский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70267.jpg?1566133762",
      "rating":{  
         "imdb":{  
            "id":9057934,
            "rating":7.8,
            "votes":26
         },
         "kinopoisk":{  
            "id":1258807,
            "rating":null,
            "votes":56
         }
      },
      "screenings":{  
         "count":6,
         "next":"2019-09-05T15:30:00+03:00"
      },
      "title":{  
         "original":"Art & Mind",
         "russian":"Разум и искусство"
      },
      "trailer":{  
         "original":"KZtV53RB1PQ",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":16,
      "cast":[  
         "Прабхас",
         "Шраддха Капур",
         "Жаклин Фернандес",
         "Мандира Беди"
      ],
      "countries":[  
         "Индия"
      ],
      "created_at":"2019-08-23T23:16:07+03:00",
      "description":"Действие фильма разворачивается в одном из крупных индийских городов. Главный герой картины – честный и неподкупный полицейский, который уже давно привык сражаться с преступностью практически без поддержки со стороны своих коррумпированных коллег. Долгое время он расследовал самые обычные дела, которые не требовали от него каких-то сверхусилий. Но однажды главный герой столкнулся с невероятно сложным и запутанным преступлением. Впервые в своей жизни он оказался один на один с врагом, способным стереть его в порошок. Это расследование станет для него самой главной проверкой на прочность. Сможет ли бесстрашный детектив раскрыть масштабный заговор и отправить преступников за решётку?",
      "directors":[  
         "Суджит"
      ],
      "duration":180,
      "genres":[  
         "боевик",
         "триллер"
      ],
      "id":70478,
      "languages":null,
      "poster":"https://msk.subscity.ru/images/posters/70478.jpg?1566987400",
      "rating":{  
         "imdb":{  
            "id":6836936,
            "rating":5.9,
            "votes":8909
         },
         "kinopoisk":{  
            "id":1044632,
            "rating":null,
            "votes":3
         }
      },
      "screenings":{  
         "count":1,
         "next":"2019-09-07T18:00:00+03:00"
      },
      "title":{  
         "original":"Saaho",
         "russian":"Саахо"
      },
      "trailer":{  
         "original":"lD0-ztCFydA",
         "russian":null
      },
      "year":2019
   },
   {  
      "age_restriction":6,
      "cast":null,
      "countries":[  
         "Япония"
      ],
      "created_at":"2019-08-05T19:01:13+03:00",
      "description":"Фантазерка Коконэ обожает спать. Где бы она ни засыпала — дома или в школе, ей всегда снится один сон. Действие происходит в королевстве, похожем на ее родной город, а принцесса Ансьен, владеющая искусством магии, как две капли воды похожа на Коконэ. Героине кажется, что фантастические сновидения как-то связаны с ее жизнью. Однажды эта догадка получит подтверждение…",
      "directors":[  
         "Кэндзи Камияма"
      ],
      "duration":111,
      "genres":[  
         "мультфильм",
         "фантастика",
         "аниме"
      ],
      "id":70227,
      "languages":[  
         "японский"
      ],
      "poster":"https://msk.subscity.ru/images/posters/70227.jpg?1566643527",
      "rating":{  
         "imdb":{  
            "id":5731132,
            "rating":6.3,
            "votes":917
         },
         "kinopoisk":{  
            "id":986939,
            "rating":6.5,
            "votes":203
         }
      },
      "screenings":{  
         "count":1,
         "next":"2019-09-08T13:05:00+03:00"
      },
      "title":{  
         "original":"Hirune-hime: Shiranai watashi no monogatari",
         "russian":"Спящая принцесса"
      },
      "trailer":{  
         "original":"JWXnyjYrhhY",
         "russian":null
      },
      "year":2017
   },
   {  
      "age_restriction":16,
      "cast":null,
      "countries":[  
         "Канада",
         "США",
         "Франция"
      ],
      "created_at":"2019-08-19T23:02:30+03:00",
      "description":"IV международный фестиваль короткометражного кино Kinematic Shorts представляет новую конкурсную программу. Шесть фильмов отобраны оргкомитетом в рамках открытого конкурса. Это игровые короткометражные работы из США, Канады и Европы, среди которых участники и победители таких фестивалей, как Tribeca, Sundance, Клермон-Ферран, Торонто.",
      "directors":[  
         "Ник Хартанто",
         "Сэм Родэн",
         "Сандхья Сури",
         "Маршалл Карри",
         "Аарон Рис",
         "Ив Пьят",
         "Уилл Киндрик"
      ],
      "duration":93,
      "genres":[  
         "короткометражный"
      ],
      "id":70414,
      "languages":null,
      "poster":"https://msk.subscity.ru/images/posters/70414.jpg?1567538455",
      "rating":{  
         "imdb":{  
            "id":null,
            "rating":null,
            "votes":null
         },
         "kinopoisk":{  
            "id":null,
            "rating":null,
            "votes":null
         }
      },
      "screenings":{  
         "count":8,
         "next":"2019-09-06T19:50:00+03:00"
      },
      "title":{  
         "original":"Kinematic Shorts 2019",
         "russian":"Фестиваль короткометражного кино «Kinematic Shorts-2019»"
      },
      "trailer":{  
         "original":null,
         "russian":null
      },
      "year":2019
   }
]
    """.trimIndent()
    }
}
