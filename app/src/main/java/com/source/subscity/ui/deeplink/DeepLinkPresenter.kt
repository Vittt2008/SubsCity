package com.source.subscity.ui.deeplink

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.source.subscity.providers.CityProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class DeepLinkPresenter @Inject constructor(private val cityProvider: CityProvider) : MvpPresenter<DeepLinkView>() {

    companion object {
        const val MAIN_URL = "https://spb.subscity.ru/" //Главная страница
        const val MOVIES_URL = "https://spb.subscity.ru/movies" //Страница фильмов
        const val MOVIE_URL = "https://spb.subscity.ru/movies/58701-the-shape-of-water" //Страница фильма
        const val CINEMAS_URL = "https://spb.subscity.ru/cinemas" //Страница кинотеатров
        const val CINEMA_URL = "https://spb.subscity.ru/cinemas/87-angleterre-cinema-lounge" //Страница кинотеатра
        const val DATES_URL = "https://spb.subscity.ru/dates/2018-03-14" //Страница с датами (их нет)

        const val MOVIES = "movies"
        const val CINEMAS = "cinemas"
    }

    fun performDeepLink(data: Uri) {
        val hosts = data.host.split('.')
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

    private fun <T> List<T>.secondOrNull(): T? {
        if (this.size >= 2) {
            return this[1]
        }
        return null
    }
}