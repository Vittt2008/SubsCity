package com.markus.subscity.ui.deeplink

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.PreferencesProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class DeepLinkPresenter @Inject constructor(private val cityProvider: CityProvider,
                                            private val preferencesProvider: PreferencesProvider) : MvpPresenter<DeepLinkView>() {

    companion object {
        const val MOVIES = "movies"
        const val CINEMAS = "cinemas"
    }

    fun performDeepLink(data: Uri) {
        val hosts = data.host.orEmpty().split('.')
        if (hosts.size == 3) {
            cityProvider.changeCity(hosts.first())
        }

        val pathSegments = data.pathSegments
        if (pathSegments.isEmpty()) {
            viewState.showMain()
        } else if (pathSegments.first().equals(MOVIES, true)) {
            if (pathSegments.size == 2) {
                val movieId = pathSegments[1].split('-').first().toLongOrNull()
                if (movieId != null) {
                    viewState.showMovie(movieId)
                } else {
                    viewState.showMovies()
                }
            } else {
                viewState.showMovies()
            }
        } else if (pathSegments.first().equals(CINEMAS, true)) {
            if (pathSegments.size == 2) {
                val cinemaId = pathSegments[1].split('-').first().toLongOrNull()
                if (cinemaId != null) {
                    viewState.showCinema(cinemaId)
                } else {
                    viewState.showCinemas()
                }
            } else {
                viewState.showCinemas()
            }
        } else {
            viewState.showMain()
        }
    }
}