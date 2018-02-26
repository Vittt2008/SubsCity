package com.source.subscity.dagger

import com.source.subscity.ui.cinemas.CinemasPresenter
import com.source.subscity.ui.movies.MoviesPresenter
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
}