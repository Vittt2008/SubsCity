package com.markus.subscity.dagger

import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.metro.MoscowMetroTextProvider
import com.markus.subscity.providers.metro.SpbMetroTextProvider
import com.markus.subscity.ui.about.AboutPresenter
import com.markus.subscity.ui.cinema.CinemaAdapter
import com.markus.subscity.ui.cinema.CinemaPresenter
import com.markus.subscity.ui.cinemas.CinemasAdapter
import com.markus.subscity.ui.cinemas.CinemasPresenter
import com.markus.subscity.ui.cinemasmap.CinemasMapPresenter
import com.markus.subscity.ui.city.CityAdapter
import com.markus.subscity.ui.city.CityPresenter
import com.markus.subscity.ui.deeplink.DeepLinkPresenter
import com.markus.subscity.ui.donate.DonatePresenter
import com.markus.subscity.ui.movie.MovieAdapter
import com.markus.subscity.ui.movie.MoviePresenter
import com.markus.subscity.ui.movies.MoviesAdapter
import com.markus.subscity.ui.movies.MoviesPresenter
import com.markus.subscity.ui.settings.SettingsPresenter
import com.markus.subscity.ui.share.SharePresenter
import com.markus.subscity.utils.Analytics
import dagger.Component
import javax.inject.Singleton


/**
 * @author Vitaliy Markus
 */
@Component(modules = [SubsCityModule::class])
@Singleton
interface SubsCityComponent {

    fun createMoviesPresenter(): MoviesPresenter
    fun createCinemasPresenter(): CinemasPresenter
    fun createSettingsPresenter(): SettingsPresenter
    fun createMoviePresenter(): MoviePresenter
    fun createCinemaPresenter(): CinemaPresenter
    fun createCityPresenter(): CityPresenter
    fun createDeepLinkPresenter(): DeepLinkPresenter
    fun createCinemasMapPresenter(): CinemasMapPresenter
    fun createAboutPresenter(): AboutPresenter
    fun createDonatePresenter(): DonatePresenter
    fun createSharePresenter(): SharePresenter

    fun createSpbMetroTextProvider(): SpbMetroTextProvider
    fun createMoscowMetroTextProvider(): MoscowMetroTextProvider

    fun inject(adapter: CinemasAdapter)
    fun inject(adapter: MovieAdapter)
    fun inject(adapter: MoviesAdapter)
    fun inject(adapter: CinemaAdapter)
    fun inject(adapter: CityAdapter)

    fun provideAnalytics(): Analytics
    fun provideCityProvider(): CityProvider

}