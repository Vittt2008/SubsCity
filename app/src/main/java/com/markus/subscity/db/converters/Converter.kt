package com.markus.subscity.db.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime


/**
 * @author Vitaliy Markus
 */
class Converter {

    companion object {

        private val gson = Gson()
        private val longListType = object : TypeToken<List<Long>>() {}.type
        private val stringListType = object : TypeToken<List<String>>() {}.type

        @JvmStatic
        @TypeConverter
        fun convertLongListToString(list: List<Long>): String {
            return gson.toJson(list)
        }

        @JvmStatic
        @TypeConverter
        fun convertStringToLongList(string: String): List<Long> {
            return gson.fromJson(string, longListType)
        }

        @JvmStatic
        @TypeConverter
        fun convertStringListToString(list: List<String>): String {
            return gson.toJson(list)
        }

        @JvmStatic
        @TypeConverter
        fun convertStringToStringList(string: String): List<String> {
            return gson.fromJson(string, stringListType)
        }

        @JvmStatic
        @TypeConverter
        fun convertDateToLong(date: DateTime): Long {
            return date.millis
        }

        @JvmStatic
        @TypeConverter
        fun convertLongToDate(long: Long): DateTime {
            return DateTime(long)
        }
    }

}