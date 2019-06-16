package com.markus.subscity.utils.analytics

import android.app.Activity
import com.markus.subscity.api.entities.screening.Screening

interface Analytics {
    fun logStartApp(city: String)
    fun logOpenRateDialog()
    fun logOpenMain()
    fun logOpenMovies(fromDeepLink: Boolean)
    fun logOpenCinemas(fromDeepLink: Boolean)
    fun logOpenCinemasMap(city: String, fromCinemas: Boolean)
    fun logOpenSettings()
    fun logOpenMovie(movieId: Long, movieName: String?, fromDeepLink: Boolean)
    fun logOpenYouTube(movieId: Long, movieName: String, trailerId: String, isOriginal: Boolean)
    fun logShareMovie(filmId: Long, filmName: String)
    fun logOpenCinema(cinemaId: Long, cinemaName: String?, fromDeepLink: Boolean)
    fun logOpenCinemaFromMap(cinemaId: Long)
    fun logCinemaAddressClick(cinemaId: Long, cinemaName: String, address: String)
    fun logCinemaPhoneClick(cinemaId: Long, cinemaName: String, phone: String)
    fun logCinemaWebClick(cinemaId: Long, cinemaName: String, url: String)
    fun logOpenAbout()
    fun logOpenPolicy()
    fun logOpenSocialNetwork(socialNetwork: String, city: String, url: String)
    fun logOpenEmail(email: String)
    fun logOpenCityPicker()
    fun logThemePicker()
    fun logLanguagePicker()
    fun logSwitchCity(city: String)
    fun logBuyTicket(screening: Screening)
    fun logOpenPlayStore(fromRateDialog: Boolean)
    fun logOpenGitHub()
    fun logActivity(activity: Activity)
}

