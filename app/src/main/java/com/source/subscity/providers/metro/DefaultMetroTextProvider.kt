package com.source.subscity.providers.metro

import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class DefaultMetroTextProvider @Inject constructor() : MetroTextProvider {

    override fun formatMetroListStation(stations: List<String>): CharSequence {
        return stations.join(" / ")
    }

}