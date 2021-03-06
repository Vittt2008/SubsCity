package com.markus.subscity

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.markus.subscity.dagger.DaggerSubsCityComponent
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.dagger.SubsCityModule
import com.markus.subscity.extensions.analytics
import com.markus.subscity.providers.PreferencesProvider
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid


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
        if (!BuildConfig.DEBUG) {
            val fabric = Fabric.Builder(this)
                    .kits(Crashlytics())
                    .debuggable(true)
                    .build()
            Fabric.with(fabric)
        }

        JodaTimeAndroid.init(this)

        val component = DaggerSubsCityComponent.builder()
                .subsCityModule(SubsCityModule(this))
                .build()
        SubsCityDagger.init(component)

        component.provideThemeProvider().applyCurrentTheme()

        val preferences = component.providePreferencesProvider().getAppPreferences()

        val launchCount = preferences.getInt(PreferencesProvider.LAUNCH_COUNT_KEY, 0)
        preferences.edit().putInt(PreferencesProvider.LAUNCH_COUNT_KEY, launchCount + 1).apply()

        val cityId = preferences.getString(PreferencesProvider.CITY_ID_KEY, null)
        if (cityId != null) {
            val cityProvider = component.provideCityProvider()
            analytics().logStartApp(cityProvider.cityName)
        }
    }
}