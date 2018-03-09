package com.source.subscity.providers

import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.metro.DefaultMetroTextProvider
import com.source.subscity.providers.metro.MetroTextProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class MetroProvider @Inject constructor(private val cityProvider: CityProvider,
                                        private val defaultMetroTextProvider: DefaultMetroTextProvider) {

    private var currentCity: String? = null
    private var currentProvider: MetroTextProvider = defaultMetroTextProvider

    val currentMetroTextProvider: MetroTextProvider
        get() {
            if (currentCity == cityProvider.city) {
                return currentProvider
            }
            currentCity = cityProvider.city
            currentProvider = when (currentCity) {
                CityProvider.SAINT_PETERSBURG -> SubsCityDagger.component.createSpbMetroTextProvider()
                CityProvider.MOSCOW -> SubsCityDagger.component.createMoscowMetroTextProvider()
                else -> defaultMetroTextProvider
            }
            return currentProvider
        }
}