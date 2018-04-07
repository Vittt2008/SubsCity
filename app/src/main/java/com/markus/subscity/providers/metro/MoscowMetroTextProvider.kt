package com.markus.subscity.providers.metro

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.markus.subscity.R
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MoscowMetroTextProvider @Inject constructor(context: Context) : MetroTextProvider {

    override fun formatMetroListStation(stations: List<String>): CharSequence {
        return stations.map { colorStation(it) }.join(" / ")
    }

    private fun colorStation(station: String): CharSequence {
        val span = SpannableString(station)
        val lowerCaseStation = station.toLowerCase()
        val color = when {
            line1.contains(lowerCaseStation) -> colorLine1
            line2.contains(lowerCaseStation) -> colorLine2
            line3.contains(lowerCaseStation) -> colorLine3
            line4.contains(lowerCaseStation) -> colorLine4
            line5.contains(lowerCaseStation) -> colorLine5
            line6.contains(lowerCaseStation) -> colorLine6
            line7.contains(lowerCaseStation) -> colorLine7
            line8.contains(lowerCaseStation) -> colorLine8
            line9.contains(lowerCaseStation) -> colorLine9
            line10.contains(lowerCaseStation) -> colorLine10
            line11.contains(lowerCaseStation) -> colorLine11
            line12.contains(lowerCaseStation) -> colorLine12
            line13.contains(lowerCaseStation) -> colorLine13
            line14.contains(lowerCaseStation) -> colorLine14
            line15.contains(lowerCaseStation) -> colorLine15
            else -> defaultColor
        }
        span.setSpan(ForegroundColorSpan(color), 0, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return span
    }

    private val defaultColor = ContextCompat.getColor(context, R.color.subtitle_color)
    private val colorLine1 = ContextCompat.getColor(context, R.color.metro_msk_line_1)
    private val colorLine2 = ContextCompat.getColor(context, R.color.metro_msk_line_2)
    private val colorLine3 = ContextCompat.getColor(context, R.color.metro_msk_line_3)
    private val colorLine4 = ContextCompat.getColor(context, R.color.metro_msk_line_4)
    private val colorLine5 = ContextCompat.getColor(context, R.color.metro_msk_line_5)
    private val colorLine6 = ContextCompat.getColor(context, R.color.metro_msk_line_6)
    private val colorLine7 = ContextCompat.getColor(context, R.color.metro_msk_line_7)
    private val colorLine8 = ContextCompat.getColor(context, R.color.metro_msk_line_8)
    private val colorLine9 = ContextCompat.getColor(context, R.color.metro_msk_line_9)
    private val colorLine10 = ContextCompat.getColor(context, R.color.metro_msk_line_10)
    private val colorLine11 = ContextCompat.getColor(context, R.color.metro_msk_line_11)
    private val colorLine12 = ContextCompat.getColor(context, R.color.metro_msk_line_12)
    private val colorLine13 = ContextCompat.getColor(context, R.color.metro_msk_line_13)
    private val colorLine14 = ContextCompat.getColor(context, R.color.metro_msk_line_14)
    private val colorLine15 = ContextCompat.getColor(context, R.color.metro_msk_line_15)

    private val line1 = listOf(
            "Библиотека имени Ленина",
            "Бульвар Рокоссовского",
            "Воробьевы горы",
            "Комсомольская",
            "Красносельская",
            "Красные ворота",
            "Кропоткинская",
            "Лубянка",
            "Охотный ряд",
            "Парк Культуры",
            "Преображенская площадь",
            "Проспект Вернадского",
            "Румянцево",
            "Саларьево",
            "Сокольники",
            "Спортивная",
            "Тропарёво",
            "Тропарево",
            "Университет",
            "Фрунзенская",
            "Черкизовская",
            "Чистые пруды",
            "Юго-Западная",
            "Филатов Луг",
            "Прокшино",
            "Ольховая",
            "Столбово").map { it.toLowerCase() }

    private val line2 = listOf(
            "Автозаводская",
            "Алма-Атинская",
            "Аэропорт",
            "Белорусская",
            "Водный стадион",
            "Войковская",
            "Динамо",
            "Домодедовская",
            "Кантемировская",
            "Каширская",
            "Коломенская",
            "Красногвардейская",
            "Маяковская",
            "Новокузнецкая",
            "Орехово",
            "Павелецкая",
            "Речной вокзал",
            "Сокол",
            "Тверская",
            "Театральная",
            "Технопарк",
            "Ховрино",
            "Царицыно",
            "Беломорская").map { it.toLowerCase() }

    private val line3 = listOf(
            "Арбатская",
            "Бауманская",
            "Волоколамская",
            "Измайловская",
            "Киевская",
            "Крылатское",
            "Кунцевская",
            "Курская",
            "Митино",
            "Молодежная",
            "Мякинино",
            "Парк Победы",
            "Партизанская",
            "Первомайская",
            "Площадь Революции",
            "Пятницкое шоссе",
            "Семеновская",
            "Славянский бульвар",
            "Смоленская",
            "Строгино",
            "Щелковская",
            "Электрозаводская").map { it.toLowerCase() }

    private val line4 = listOf(
            "Александровский сад",
            "Арбатская",
            "Багратионовская",
            "Выставочная",
            "Киевская",
            "Кунцевская",
            "Кутузовская",
            "Международная",
            "Пионерская",
            "Смоленская",
            "Студенческая",
            "Филевский парк",
            "Фили").map { it.toLowerCase() }

    private val line5 = listOf(
            "Белорусская",
            "Добрынинская",
            "Киевская",
            "Комсомольская",
            "Краснопресненская",
            "Курская",
            "Новослободская",
            "Октябрьская",
            "Павелецкая",
            "Парк Культуры",
            "Проспект Мира",
            "Таганская").map { it.toLowerCase() }

    private val line6 = listOf(
            "Академическая",
            "Алексеевская",
            "Бабушкинская",
            "Беляево",
            "Ботанический сад",
            "ВДНХ",
            "Калужская",
            "Китай-город",
            "Коньково",
            "Ленинский проспект",
            "Медведково",
            "Новоясеневская",
            "Новые Черёмушки",
            "Новые Черемушки",
            "Октябрьская",
            "Проспект Мира",
            "Профсоюзная",
            "Рижская",
            "Свиблово",
            "Сухаревская",
            "Теплый стан",
            "Третьяковская",
            "Тургеневская",
            "Шаболовская",
            "Ясенево").map { it.toLowerCase() }

    private val line7 = listOf(
            "Баррикадная",
            "Беговая",
            "Волгоградский проспект",
            "Выхино",
            "Жулебино",
            "Китай-город",
            "Котельники",
            "Кузнецкий мост",
            "Кузьминки",
            "Лермонтовский проспект",
            "Октябрьское поле",
            "Планерная",
            "Полежаевская",
            "Пролетарская",
            "Пушкинская",
            "Рязанский проспект",
            "Спартак",
            "Сходненская",
            "Таганская",
            "Текстильщики",
            "Тушинская",
            "Улица 1905 года",
            "Щукинская").map { it.toLowerCase() }

    private val line8 = listOf(
            "Авиамоторная",
            "Ломоносовский проспект",
            "Марксистская",
            "Минская",
            "Новогиреево",
            "Новокосино",
            "Парк Победы",
            "Перово",
            "Петровский парк",
            "Площадь Ильича",
            "Раменки",
            "Третьяковская",
            "Хорошёвская",
            "Хорошевская",
            "ЦСКА",
            "Шелепиха",
            "Шоссе Энтузиастов",
            "Мичуринский проспект",
            "Очаково",
            "Говорово",
            "Солнцево",
            "Боровское шоссе",
            "Новопеределкино",
            "Рассказовка").map { it.toLowerCase() }

    private val line9 = listOf(
            "Алтуфьево",
            "Аннино",
            "Бибирево",
            "Боровицкая",
            "Бульвар Дмитрия Донского",
            "Владыкино",
            "Дмитровская",
            "Менделеевская",
            "Нагатинская",
            "Нагорная",
            "Нахимовский Проспект",
            "Отрадное",
            "Петровско-Разумовская",
            "Полянка",
            "Пражская",
            "Савеловская",
            "Севастопольская",
            "Серпуховская",
            "Тимирязевская",
            "Тульская",
            "Улица академика Янгеля",
            "Цветной бульвар",
            "Чертановская",
            "Чеховская",
            "Южная").map { it.toLowerCase() }

    private val line10 = listOf(
            "Борисово",
            "Братиславская",
            "Бутырская",
            "Волжская",
            "Достоевская",
            "Дубровка",
            "Зябликово",
            "Кожуховская",
            "Крестьянская застава",
            "Люблино",
            "Марьина роща",
            "Марьино",
            "Петровско-Разумовская",
            "Печатники",
            "Римская",
            "Сретенский бульвар",
            "Трубная",
            "Фонвизинская",
            "Чкаловская",
            "Шипиловская",
            "Окружная",
            "Верхние Лихоборы",
            "Селигерская").map { it.toLowerCase() }

    private val line11 = listOf(
            "Варшавская",
            "Каховская",
            "Каширская",
            "Нижняя Масловка",
            "Шереметьевская",
            "Ржевская",
            "Стромынка",
            "Рубцовская",
            "Лефортово",
            "Авиамоторная",
            "Нижегородская улица",
            "Текстильщики",
            "Печатники",
            "Нагатинский Затон",
            "Кленовый бульвар",
            "Зюзино",
            "Воронцовская",
            "Улица Новаторов",
            "Проспект Вернадского",
            "Мичуринский проспект",
            "Аминьевское шоссе",
            "Давыдково",
            "Можайская",
            "Терехово",
            "Нижние Мнёвники",
            "Нижние Мневники",
            "Улица Народного Ополчения").map { it.toLowerCase() }

    private val line12 = listOf(
            "Битцевский парк",
            "Бульвар адмирала Ушакова",
            "Бунинская Аллея",
            "Лесопарковая",
            "Улица Горчакова",
            "Улица Скобелевская",
            "Улица Старокачаловская").map { it.toLowerCase() }

    private val line13 = listOf(
            "Выставочный центр",
            "Телецентр",
            "Тимирязевская",
            "Улица Академика Королёва",
            "Улица Академика Королева",
            "Улица Милашенкова",
            "Улица Сергея Эйзенштейна").map { it.toLowerCase() }

    private val line14 = listOf(
            "Автозаводская",
            "Андроновка",
            "Балтийская",
            "Белокаменная",
            "Ботанический сад",
            "Бульвар Рокоссовского",
            "Верхние Котлы",
            "Владыкино",
            "Деловой центр",
            "Дубровка",
            "ЗИЛ",
            "Зорге",
            "Измайлово",
            "Коптево",
            "Крымская",
            "Кутузовская",
            "Лихоборы",
            "Локомотив",
            "Лужники",
            "Нижегородская",
            "Новохохловская",
            "Окружная",
            "Панфиловская",
            "Площадь Гагарина",
            "Ростокино",
            "Соколиная Гора",
            "Стрешнево",
            "Угрешская",
            "Хорошёво",
            "Хорошево",
            "Шелепиха",
            "Шоссе Энтузиастов").map { it.toLowerCase() }

    private val line15 = listOf(
            "Деловой центр",
            "Петровский парк",
            "Хорошёвская",
            "Хорошевская",
            "ЦСКА",
            "Шелепиха",
            "Нижегородская улица",
            "Стахановская",
            "Окская улица",
            "Юго-Восточная",
            "Косино",
            "Улица Дмитриевского",
            "Лухмановская",
            "Некрасовка").map { it.toLowerCase() }
}