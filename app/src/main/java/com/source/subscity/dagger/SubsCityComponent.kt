package com.source.subscity.dagger

import com.source.subscity.ui.cinemas.CinemasPresenter
import com.source.subscity.ui.movies.MoviesPresenter
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
}