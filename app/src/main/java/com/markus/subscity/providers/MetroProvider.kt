package com.markus.subscity.providers

import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.metro.DefaultMetroTextProvider
import com.markus.subscity.providers.metro.MetroTextProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class MetroProvider @Inject constructor(private val cityProvider: CityProvider,
                                        defaultMetroTextProvider: DefaultMetroTextProvider) {

    private var currentCityId: String? = null
    private var currentProvider: MetroTextProvider = defaultMetroTextProvider

    val currentMetroTextProvider: MetroTextProvider
        get() {
            if (currentCityId == cityProvider.cityId) {
                return currentProvider
            }
            currentCityId = cityProvider.cityId
            currentProvider = when (currentCityId) {
                CityProvider.SAINT_PETERSBURG -> SubsCityDagger.component.createSpbMetroTextProvider()
                CityProvider.MOSCOW -> SubsCityDagger.component.createMoscowMetroTextProvider()
                else -> throw IllegalArgumentException("Not supported cityId = $currentCityId")
            }
            return currentProvider
        }
}