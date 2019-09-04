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
        const val LAUNCH_COUNT_KEY = "launch_count"
        const val WAS_RANKED_KEY = "was_ranked"
        const val WAS_DONATED_KEY = "was_donated"
        const val APP_THEME_KEY = "app_theme"
    }

    fun getAppPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}