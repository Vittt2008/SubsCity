package com.markus.subscity.ui.deeplink

import com.arellomobile.mvp.MvpView

/**
 * @author Vitaliy Markus
 */
interface DeepLinkView : MvpView {
    fun showMain()
    fun showMovies()
    fun showCinemas()
    fun showMovie(movieId: Long)
    fun showCinema(cinemaId: Long)
    fun showCityPicker()
}