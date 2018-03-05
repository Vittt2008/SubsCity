package com.source.subscity.providers

import android.content.Context
import com.source.subscity.R
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class DurationProvider @Inject constructor(private val context: Context) {

    private val MINUTES_IN_HOUR = 60

    fun format(min: Int): String {
        return context.getString(R.string.duration_provider_format, min / MINUTES_IN_HOUR, min % MINUTES_IN_HOUR)
    }
}