package com.source.subscity.api.client

import com.google.gson.Gson
import com.source.subscity.api.services.SubsCityService
import io.reactivex.Scheduler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Vitaliy Markus
 */
class SubsCityClient(okHttpClient: OkHttpClient,
                     gson: Gson,
                     scheduler: Scheduler) {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost") //ignored
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
            .build()

    val subsCityService by lazy { retrofit.create(SubsCityService::class.java) }
}