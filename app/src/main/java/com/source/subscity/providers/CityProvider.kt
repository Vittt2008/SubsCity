package com.source.subscity.providers

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class CityProvider @Inject constructor(context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "subs_city.xml"
        private const val CITY_KEY = "city"

        const val SAINT_PETERSBURG = "spb"
        const val MOSCOW = "msk"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    var city = sharedPreferences.getString(CITY_KEY, SAINT_PETERSBURG)
        set(value) {
            field = value
            sharedPreferences.edit().putString(CITY_KEY, value).apply()
        }
}