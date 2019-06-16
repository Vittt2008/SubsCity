package com.markus.subscity.dagger

import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.PreferencesProvider
import com.markus.subscity.providers.ThemeProvider
import com.markus.subscity.providers.metro.MoscowMetroTextProvider
import com.markus.subscity.providers.metro.SpbMetroTextProvider
import com.markus.subscity.ui.about.AboutPresenter
import com.markus.subscity.ui.cinema.CinemaPresenter
import com.markus.subscity.ui.cinema.delegates.CinemaInfoDelegate
import com.markus.subscity.ui.cinema.delegates.MovieScreeningsDelegate
import com.markus.subscity.ui.cinemas.CinemasAdapter
import com.markus.subscity.ui.cinemas.CinemasPresenter
import com.markus.subscity.ui.cinemasmap.CinemasMapPresenter
import com.markus.subscity.ui.city.CityAdapter
import com.markus.subscity.ui.city.CityPresenter
import com.markus.subscity.ui.deeplink.DeepLinkPresenter
import com.markus.subscity.ui.main.MainPresenter
import com.markus.subscity.ui.movie.MoviePresenter
import com.markus.subscity.ui.movie.delegates.CinemaScreeningsDelegate
import com.markus.subscity.ui.movie.delegates.MovieInfoDelegate
import com.markus.subscity.ui.movies.MoviesAdapter
import com.markus.subscity.ui.movies.MoviesPresenter
import com.markus.subscity.ui.settings.SettingsPresenter
import com.markus.subscity.ui.share.SharePresenter
import com.markus.subscity.ui.splash.SplashPresenter
import com.markus.subscity.ui.theme.ThemePresenter
import com.markus.subscity.utils.analytics.Analytics
import dagger.Component
import javax.inject.Singleton


/**
 * @author Vitaliy Markus
 */
@Component(modules = [SubsCityModule::class])
@Singleton
interface SubsCityComponent {

    fun createMainPresenter(): MainPresenter
    fun createMoviesPresenter(): MoviesPresenter
    fun createCinemasPresenter(): CinemasPresenter
    fun createSettingsPresenter(): SettingsPresenter
    fun createMoviePresenter(): MoviePresenter
    fun createCinemaPresenter(): CinemaPresenter
    fun createCityPresenter(): CityPresenter
    fun createDeepLinkPresenter(): DeepLinkPresenter
    fun createCinemasMapPresenter(): CinemasMapPresenter
    fun createAboutPresenter(): AboutPresenter
    fun createSharePresenter(): SharePresenter
    fun createSplashPresenter(): SplashPresenter
    fun createThemePresenter(): ThemePresenter

    fun createSpbMetroTextProvider(): SpbMetroTextProvider
    fun createMoscowMetroTextProvider(): MoscowMetroTextProvider

    fun inject(adapter: CinemasAdapter)
    fun inject(adapter: MoviesAdapter)
    fun inject(adapter: CityAdapter)

    fun inject(delegate: CinemaInfoDelegate)
    fun inject(delegate: MovieScreeningsDelegate)
    fun inject(delegate: MovieInfoDelegate)
    fun inject(delegate: CinemaScreeningsDelegate)

    fun provideAnalytics(): Analytics
    fun provideCityProvider(): CityProvider
    fun providePreferencesProvider(): PreferencesProvider
    fun provideThemeProvider(): ThemeProvider

}