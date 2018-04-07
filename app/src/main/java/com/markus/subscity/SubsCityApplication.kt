package com.markus.subscity

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.markus.subscity.dagger.DaggerSubsCityComponent
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.dagger.SubsCityModule
import com.markus.subscity.extensions.analytics
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

        private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
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

        val preferences = SubsCityDagger.component.providePreferencesProvider().getAppPreferences()
        val isFirstLaunch = preferences.getBoolean(KEY_IS_FIRST_LAUNCH, true)
        if (!isFirstLaunch) {
            val cityProvider = SubsCityDagger.component.provideCityProvider()
            analytics().logStartApp(cityProvider.cityName)
        } else {
            preferences.edit().putBoolean(KEY_IS_FIRST_LAUNCH, false).apply()
        }
    }
}