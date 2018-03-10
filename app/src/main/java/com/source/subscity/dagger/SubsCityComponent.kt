package com.source.subscity.dagger

import com.source.subscity.providers.metro.MoscowMetroTextProvider
import com.source.subscity.providers.metro.SpbMetroTextProvider
import com.source.subscity.ui.cinema.CinemaAdapter
import com.source.subscity.ui.cinema.CinemaPresenter
import com.source.subscity.ui.cinemas.CinemasAdapter
import com.source.subscity.ui.cinemas.CinemasPresenter
import com.source.subscity.ui.city.CityAdapter
import com.source.subscity.ui.city.CityPresenter
import com.source.subscity.ui.movie.MovieAdapter
import com.source.subscity.ui.movie.MoviePresenter
import com.source.subscity.ui.movies.MoviesAdapter
import com.source.subscity.ui.movies.MoviesPresenter
import com.source.subscity.ui.settings.SettingsAdapter
import com.source.subscity.ui.settings.SettingsPresenter
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

    fun createSpbMetroTextProvider(): SpbMetroTextProvider
    fun createMoscowMetroTextProvider(): MoscowMetroTextProvider

    fun inject(adapter: CinemasAdapter)
    fun inject(adapter: MovieAdapter)
    fun inject(adapter: MoviesAdapter)
    fun inject(adapter: CinemaAdapter)
    fun inject(adapter: CityAdapter)

}