package com.source.subscity

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.source.subscity.dagger.DaggerSubsCityComponent
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.dagger.SubsCityModule
import io.fabric.sdk.android.Fabric


/**
 * @author Vitaliy Markus
 */
class SubsCityApplication : MultiDexApplication() {

    companion object {
        const val MAIN_URL = "https://%s.subscity.ru/" //Главная страница
        const val MOVIES_URL = "https://%s.subscity.ru/movies" //Страница фильмов
        const val MOVIE_URL = "https://%s.subscity.ru/movies/%s" //Страница фильма
        const val CINEMAS_URL = "https://%s.subscity.ru/cinemas" //Страница кинотеатров
        const val CINEMA_URL = "https://%s.subscity.ru/cinemas/%s" //Страница кинотеатра
        const val DATES_URL = "https://%s.subscity.ru/dates/%s" //Страница с датами (их нет)
    }

    override fun onCreate() {
        super.onCreate()
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        val subsCityComponent = DaggerSubsCityComponent.builder()
                .subsCityModule(SubsCityModule(this))
                .build()
        SubsCityDagger.init(subsCityComponent)
    }
}