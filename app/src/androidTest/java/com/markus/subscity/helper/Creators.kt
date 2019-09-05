package com.markus.subscity.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.markus.subscity.api.deserializers.DateTimeDeserializer
import com.markus.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.markus.subscity.impl.TestCachedRepository
import com.markus.subscity.impl.TestDateTimeProvider
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.DatabaseProvider
import com.markus.subscity.providers.DateTimeProvider
import com.markus.subscity.providers.PreferencesProvider
import org.joda.time.DateTime

/**
 * @author Vitaliy Markus
 */
fun createDateTimeProvider(): TestDateTimeProvider {
    return TestDateTimeProvider()
}

fun createDatabaseProvider(context: Context): DatabaseProvider {
    val preferencesProvider = PreferencesProvider(context)
    val cityProvider = CityProvider(context, preferencesProvider)
    return DatabaseProvider(context, cityProvider)
}

fun createCachedRepository(context: Context, dateTimeProvider: DateTimeProvider): TestCachedRepository {
    val databaseProvider = createDatabaseProvider(context)
    return TestCachedRepository(databaseProvider, dateTimeProvider)
}

fun createGson(): Gson {
    return GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer)
            .registerTypeAdapterFactory(SubsCityTypeAdapterFactory)
            .create()
}