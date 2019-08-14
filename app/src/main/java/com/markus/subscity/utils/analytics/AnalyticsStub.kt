package com.markus.subscity.utils.analytics

import android.app.Activity
import com.markus.subscity.api.entities.screening.Screening

/**
 * @author Vitaliy Markus
 */
class AnalyticsStub : Analytics {
    override fun logStartApp(city: String) = Unit
    override fun logOpenRateDialog() = Unit
    override fun logOpenMain() = Unit
    override fun logOpenMovies(fromDeepLink: Boolean) = Unit
    override fun logOpenCinemas(fromDeepLink: Boolean) = Unit
    override fun logOpenCinemasMap(city: String, fromCinemas: Boolean) = Unit
    override fun logOpenSettings() = Unit
    override fun logOpenMovie(movieId: Long, movieName: String?, fromDeepLink: Boolean) = Unit
    override fun logOpenYouTube(movieId: Long, movieName: String, trailerId: String, isOriginal: Boolean) = Unit
    override fun logShareMovie(filmId: Long, filmName: String) = Unit
    override fun logOpenCinema(cinemaId: Long, cinemaName: String?, fromDeepLink: Boolean) = Unit
    override fun logOpenCinemaFromMap(cinemaId: Long) = Unit
    override fun logCinemaAddressClick(cinemaId: Long, cinemaName: String, address: String) = Unit
    override fun logCinemaPhoneClick(cinemaId: Long, cinemaName: String, phone: String) = Unit
    override fun logCinemaWebClick(cinemaId: Long, cinemaName: String, url: String) = Unit
    override fun logOpenAbout() = Unit
    override fun logOpenPolicy() = Unit
    override fun logOpenSocialNetwork(socialNetwork: String, city: String, url: String) = Unit
    override fun logOpenEmail(email: String) = Unit
    override fun logOpenCityPicker() = Unit
    override fun logThemePicker() = Unit
    override fun logLanguagePicker() = Unit
    override fun logSwitchCity(city: String) = Unit
    override fun logBuyTicket(screening: Screening) = Unit
    override fun logOpenPlayStore(fromRateDialog: Boolean) = Unit
    override fun logOpenGitHub() = Unit
    override fun logActivity(activity: Activity) = Unit
}