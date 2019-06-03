package com.markus.subscity.providers

import androidx.room.Room
import android.content.Context
import com.markus.subscity.db.DatabaseClient
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class DatabaseProvider @Inject constructor(private val context: Context,
                                           private val cityProvider: CityProvider) {

    private var currentCityId: String? = null
    private var currentClient: DatabaseClient? = null

    val currentDatabaseClient: DatabaseClient
        get() {
            if (currentCityId == cityProvider.cityId && currentClient != null) {
                return currentClient!!
            }

            currentClient?.close()

            currentCityId = cityProvider.cityId
            currentClient = when (currentCityId) {
                CityProvider.SAINT_PETERSBURG -> createDatabaseClient(CityProvider.SAINT_PETERSBURG)
                CityProvider.MOSCOW -> createDatabaseClient(CityProvider.MOSCOW)
                else -> throw IllegalArgumentException("Not supported cityId = $currentCityId")
            }
            return currentClient!!
        }

    private fun createDatabaseClient(city: String): DatabaseClient {
        return Room.databaseBuilder(context, DatabaseClient::class.java, "subs_city_$city").build()
    }
}