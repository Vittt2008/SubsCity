package com.source.subscity.providers

import android.content.Context
import com.source.subscity.R
import com.source.subscity.api.entities.City
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class CityProvider @Inject constructor(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "subs_city.xml"
        private const val CITY_ID_KEY = "city_id"

        const val SAINT_PETERSBURG = "spb"
        const val MOSCOW = "msk"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
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
            City(SAINT_PETERSBURG, context.getString(R.string.saint_petersburg), 59.95, 30.31667, 11),
            City(MOSCOW, context.getString(R.string.moscow), 55.75583,37.61778, 10)))

    val asyncCity: Observable<String> = citySubject

    fun changeCity(city: String) {
        if (this.cityId == city) {
            return
        }

        this.cityId = city
        this.citySubject.onNext(city)
    }
}