package com.markus.subscity.providers

import android.content.Context
import com.markus.subscity.R
import com.markus.subscity.api.entities.movie.Movie
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class LanguageProvider @Inject constructor(private val context: Context) {

    private val ending = context.resources.getString(R.string.language_provider_ending)

    fun languageFormat(movie: Movie): String? {
        val language = movie.languages.firstOrNull() ?: return null
        if (language.endsWith(ending)) {
            return context.resources.getString(R.string.language_provider_format_declinable, language.substring(0, language.length - ending.length))
        }
        return context.resources.getString(R.string.language_provider_format_undeclinable, language)
    }
}