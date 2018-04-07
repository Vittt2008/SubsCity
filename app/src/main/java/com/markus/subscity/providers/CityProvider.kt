package com.markus.subscity.providers

import android.content.Context
import com.markus.subscity.R
import com.markus.subscity.api.entities.City
import com.markus.subscity.api.entities.Location
import com.markus.subscity.api.entities.SocialNetworks
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class CityProvider @Inject constructor(private val context: Context,
                                       preferencesProvider: PreferencesProvider) {

    companion object {
        private const val CITY_ID_KEY = "city_id"

        const val SAINT_PETERSBURG = "spb"
        const val MOSCOW = "msk"
    }

    private val sharedPreferences = preferencesProvider.getAppPreferences()
    private val citySubject = BehaviorSubject.create<String>()

    var cityId = sharedPreferences.getString(CITY_ID_KEY, SAINT_PETERSBURG)!!
        private set(value) {
            field = value
            sharedPreferences.edit().putString(CITY_ID_KEY, value).apply()
        }

    init {
        citySubject.onNext(cityId)
    }

    val cityName: String
        get() = when (cityId) {
            SAINT_PETERSBURG -> context.getString(R.string.saint_petersburg)
            MOSCOW -> context.getString(R.string.moscow)
            else -> throw IllegalArgumentException("Not supported cityId = $cityId")
        }

    val city: City
        get() = supportedCities.blockingGet().first { it.id == cityId }

    val supportedCities = Single.just(listOf(
            City(
                    SAINT_PETERSBURG,
                    context.getString(R.string.saint_petersburg),
                    Location(59.95, 30.31667, 11),
                    SocialNetworks("https://t.me/subscity_spb", "https://vk.com/subscity_spb", "https://www.facebook.com/subscity.spb")
            ),
            City(
                    MOSCOW,
                    context.getString(R.string.moscow),
                    Location(55.75583, 37.61778, 10),
                    SocialNetworks("https://t.me/subscity_msk", "https://vk.com/subscity_msk", "https://www.facebook.com/subscity.msk")
            )
    ))

    val asyncCity: Observable<String> = citySubject

    fun changeCity(city: String) {
        if (this.cityId == city) {
            return
        }

        this.cityId = city
        this.citySubject.onNext(city)
    }
}