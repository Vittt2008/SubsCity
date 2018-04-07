package com.markus.subscity.providers

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class PreferencesProvider @Inject constructor(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "subs_city.xml"
        const val CITY_ID_KEY = "city_id"
    }

    fun getAppPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}