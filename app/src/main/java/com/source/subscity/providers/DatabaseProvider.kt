package com.source.subscity.providers

import android.arch.persistence.room.Room
import android.content.Context
import com.source.subscity.db.DatabaseClient
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class DatabaseProvider @Inject constructor(private val context: Context,
                                           private val cityProvider: CityProvider) {

    private var currentCity: String? = null
    private var currentClient: DatabaseClient? = null

    val currentDatabaseClient: DatabaseClient
        get() {
            if (currentCity == cityProvider.city && currentClient != null) {
                return currentClient!!
            }

            currentClient?.close()

            currentCity = cityProvider.city
            currentClient = when (currentCity) {
                CityProvider.SAINT_PETERSBURG -> createDatabaseClient(CityProvider.SAINT_PETERSBURG)
                CityProvider.MOSCOW -> createDatabaseClient(CityProvider.MOSCOW)
                else -> throw IllegalArgumentException("Not supported city = $currentCity")
            }
            return currentClient!!
        }

    private fun createDatabaseClient(city: String): DatabaseClient {
        return Room.databaseBuilder(context, DatabaseClient::class.java, "subs_city_$city").build()
    }
}