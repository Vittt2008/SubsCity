package com.markus.subscity.dagger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.markus.subscity.BuildConfig
import com.markus.subscity.api.ApiClient
import com.markus.subscity.api.deserializers.DateTimeDeserializer
import com.markus.subscity.api.deserializers.SubsCityTypeAdapterFactory
import com.markus.subscity.providers.DateTimeProvider
import com.markus.subscity.providers.DateTimeProviderImpl
import com.markus.subscity.utils.analytics.Analytics
import com.markus.subscity.utils.analytics.AnalyticsImpl
import com.markus.subscity.utils.analytics.AnalyticsStub
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Module
class SubsCityModule(@get:Provides val context: Context) {

    companion object {
        private const val TIMEOUT = 10_000L
    }

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .applyIf(BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
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

    @Provides
    @Singleton
    fun dateTimeProvider(dateTimeProvider: DateTimeProviderImpl): DateTimeProvider {
        return dateTimeProvider
    }

    @Provides
    @Singleton
    fun analytics(context: Context): Analytics {
        return if (BuildConfig.DEBUG) AnalyticsStub() else AnalyticsImpl(context)
    }

    private inline fun <T> T.applyIf(boolean: Boolean, block: T.() -> Unit): T {
        if (boolean) {
            block()
        }
        return this
    }
}