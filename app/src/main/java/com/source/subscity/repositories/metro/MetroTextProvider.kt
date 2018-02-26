package com.source.subscity.repositories.metro

import android.text.SpannableStringBuilder

/**
 * @author Vitaliy Markus
 */
interface MetroTextProvider {

    fun formatMetroListStation(stations: List<String>): CharSequence

    fun List<CharSequence>.join(delimiter: String): CharSequence {
        val builder = SpannableStringBuilder()
        val it = this.iterator()
        if (it.hasNext()) {
            builder.append(it.next())
            while (it.hasNext()) {
                builder.append(delimiter)
                builder.append(it.next())
            }
        }
        return builder
    }
}