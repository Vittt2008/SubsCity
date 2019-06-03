package com.markus.subscity.providers.metro

import android.content.Context
import androidx.core.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.markus.subscity.R
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class SpbMetroTextProvider @Inject constructor(context: Context) : MetroTextProvider {

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
            else -> defaultColor
        }
        span.setSpan(ForegroundColorSpan(color), 0, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return span
    }

    private val defaultColor = ContextCompat.getColor(context, R.color.subtitle_color)
    private val colorLine1 = ContextCompat.getColor(context, R.color.metro_spb_line_1)
    private val colorLine2 = ContextCompat.getColor(context, R.color.metro_spb_line_2)
    private val colorLine3 = ContextCompat.getColor(context, R.color.metro_spb_line_3)
    private val colorLine4 = ContextCompat.getColor(context, R.color.metro_spb_line_4)
    private val colorLine5 = ContextCompat.getColor(context, R.color.metro_spb_line_5)
    private val colorLine6 = ContextCompat.getColor(context, R.color.metro_spb_line_6)

    private val line1 = listOf(
            "Девяткино",
            "Гражданский проспект",
            "Академическая",
            "Политехническая",
            "Площадь Мужества",
            "Лесная",
            "Выборгская",
            "Площадь Ленина",
            "Чернышевская",
            "Площадь Восстания",
            "Владимирская",
            "Пушкинская",
            "Технологический институт",
            "Балтийская",
            "Нарвская",
            "Кировский завод",
            "Автово",
            "Ленинский проспект",
            "Проспект Ветеранов").map { it.toLowerCase() }

    private val line2 = listOf(
            "Парнас",
            "Проспект Просвещения",
            "Озерки",
            "Удельная",
            "Пионерская",
            "Чёрная речка",
            "Черная речка",
            "Петроградская",
            "Горьковская",
            "Невский проспект",
            "Сенная площадь",
            "Технологический институт",
            "Фрунзенская",
            "Московские ворота",
            "Электросила",
            "Парк Победы",
            "Московская",
            "Звёздная",
            "Звездная",
            "Купчино").map { it.toLowerCase() }

    private val line3 = listOf(
            "Планерная",
            "Туристская",
            "Беговая",
            "Новокрестовская",
            "Приморская",
            "Василеостровская",
            "Гостиный Двор",
            "Маяковская",
            "Площадь Александра Невского",
            "Елизаровская",
            "Ломоносовская",
            "Пролетарская",
            "Обухово",
            "Рыбацкое").map { it.toLowerCase() }

    private val line4 = listOf(
            "Морской фасад",
            "Гавань",
            "Горный институт",
            "Театральная",
            "Спасская",
            "Достоевская",
            "Лиговский проспект",
            "Площадь Александра Невского",
            "Новочеркасская",
            "Ладожская",
            "Проспект Большевиков",
            "Улица Дыбенко",
            "Кудрово",
            "Юго-Восточная").map { it.toLowerCase() }

    private val line5 = listOf(
            "Шуваловский проспект",
            "Комендантский проспект",
            "Старая Деревня",
            "Крестовский остров",
            "Чкаловская",
            "Спортивная",
            "Адмиралтейская",
            "Садовая",
            "Звенигородская",
            "Обводный канал",
            "Волковская",
            "Бухарестская",
            "Международная",
            "Проспект Славы",
            "Дунайская",
            "Шушары").map { it.toLowerCase() }

    private val line6 = listOf(
            "Каретная",
            "Боровая",
            "Заставская",
            "Броневая",
            "Путиловская",
            "Юго-Западная").map { it.toLowerCase() }
}