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
        private const val CITY_KEY = "city"

        const val SAINT_PETERSBURG = "spb"
        const val MOSCOW = "msk"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val citySubject = BehaviorSubject.create<String>()

    var city = sharedPreferences.getString(CITY_KEY, SAINT_PETERSBURG)!!
        private set(value) {
            field = value
            sharedPreferences.edit().putString(CITY_KEY, value).apply()
        }

    init {
        citySubject.onNext(city)
    }

    val cityName: String
        get() = when (city) {
            SAINT_PETERSBURG -> context.getString(R.string.saint_petersburg)
            MOSCOW -> context.getString(R.string.moscow)
            else -> throw IllegalArgumentException("Not supported city = $city")
        }

    val supportedCities = Single.just(listOf(
            City(SAINT_PETERSBURG, context.getString(R.string.saint_petersburg)),
            City(MOSCOW, context.getString(R.string.moscow))))

    val asyncCity: Observable<String> = citySubject

    fun changeCity(city: String) {
        if (this.city == city){
            return
        }

        this.city = city
        this.citySubject.onNext(city)
    }
}