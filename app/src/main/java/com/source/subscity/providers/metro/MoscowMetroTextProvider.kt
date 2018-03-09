package com.source.subscity.providers.metro

import android.content.Context
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MoscowMetroTextProvider @Inject constructor(context: Context) : MetroTextProvider {

    override fun formatMetroListStation(stations: List<String>): CharSequence {
        return stations.join(" / ")
    }

}