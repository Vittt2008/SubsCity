package com.source.subscity.dagger

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.source.subscity.api.ApiClient
import com.source.subscity.api.deserializers.DateTimeDeserializer
import com.source.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.source.subscity.db.DatabaseClient
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Module
class SubsCityModule(@get:Provides val context: Context) {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor {
                    Thread.sleep(5000)
                    it.proceed(it.request())
                }
                .build()
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer)
                .registerTypeAdapterFactory(SubsCityTypeAdapterFactory)
                .serializeNulls()
                .create()
    }

    @Provides
    @Singleton
    fun apiClient(okHttpClient: OkHttpClient, gson: Gson): ApiClient {
        return ApiClient(okHttpClient, gson, Schedulers.io())
    }
}