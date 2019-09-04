package com.markus.subscity.providers

import android.content.Context
import android.os.Build
import com.markus.subscity.R
import java.util.*
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class DisplayLanguageProvider @Inject constructor(private val context: Context) {

    /**
     * Display language derived from Android's language settings and translates available.
     * To be short it always represent the actual language a user sees in the app.
     */
    val displayLanguage by lazy { context.displayLanguage }

    /**
     * This is default locale for the Android environment
     * may be different from the configuration that is taken from resources to display texts
     */
    val currentLocale by lazy { context.currentLocale }

    private val Context.displayLanguage: Locale
        get() {
            val currentUserLanguageCode = getString(R.string.current_language).split("-")
            val languageCode = currentUserLanguageCode.getOrNull(0) ?: return Locale.getDefault()
            val countryCode = currentUserLanguageCode.getOrElse(1) { currentLocale.country }
            return Locale(languageCode, countryCode)
        }

    private val Context.currentLocale: Locale
        get() {
            val configuration = resources.configuration
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.locales.get(0)
            } else {
                configuration.locale
            }
        }
}

val DisplayLanguageProvider.isRussian: Boolean
    get() = displayLanguage.language == "ru"