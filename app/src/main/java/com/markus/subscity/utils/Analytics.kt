package com.markus.subscity.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.markus.subscity.BuildConfig
import com.markus.subscity.api.entities.screening.Screening
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vitaliy Markus
 */
@Singleton
class Analytics @Inject constructor(context: Context) {

    companion object {

        // Events

        private const val EVENT_APP = "event_app"

        private const val EVENT_RATE_DIALOG = "event_rate_dialog"

        private const val EVENT_MAIN = "event_main"
        private const val EVENT_FILMS = "event_films"
        private const val EVENT_CINEMAS = "event_cinemas"
        private const val EVENT_CINEMAS_MAP = "event_cinemas_map"
        private const val EVENT_SETTINGS = "event_settings"

        private const val EVENT_MOVIE = "event_film"
        private const val EVENT_YOUTUBE = "event_youtube"
        private const val EVENT_SHARE = "event_share"

        private const val EVENT_CINEMA = "event_cinema"
        private const val EVENT_CINEMA_FROM_MAP = "event_cinema_from_map"
        private const val EVENT_CINEMA_ADDRESS = "event_cinema_address"
        private const val EVENT_CINEMA_DIALER = "event_cinema_dialer"
        private const val EVENT_CINEMA_WEB = "event_cinema_web"

        private const val EVENT_ABOUT = "event_about"
        private const val EVENT_POLICY = "event_policy"
        private const val EVENT_SOCIAL_NETWORK = "event_social_network"
        private const val EVENT_EMAIL = "event_email"
        private const val EVENT_CITY_PICKER = "event_city_chooser"
        private const val EVENT_THEME_PICKER = "event_theme_chooser"
        private const val EVENT_LANGUAGE_PICKER = "event_language_chooser"
        private const val EVENT_SWITCH_CITY = "event_choose_city"

        private const val EVENT_BUY_TICKET = "event_buy_ticket"

        private const val EVENT_OPEN_PLAY_STORE = "event_open_play_store"
        private const val EVENT_OPEN_GIT_HUB = "event_open_git_hub"

        // Keys

        private const val KEY_CITY = "city"
        private const val KEY_FROM = "from"
        private const val KEY_MOVIE_ID = "film_id"
        private const val KEY_MOVIE_NAME = "film_name"
        private const val KEY_TRAILER_ID = "trailer_id"
        private const val KEY_CINEMA_ID = "cinema_id"
        private const val KEY_CINEMA_NAME = "cinema_name"
        private const val KEY_CINEMA_ADDRESS = "cinema_address"
        private const val KEY_CINEMA_PHONE = "cinema_phone"
        private const val KEY_CINEMA_WEB = "cinema_web"
        private const val KEY_SOCIAL_NETWORK = "social_network"
        private const val KEY_SOCIAL_NETWORK_URL = "social_network_url"
        private const val KEY_FROM_DEEP_LINK = "from_deep_link"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_SCREENING_ID = "screening_id"
        private const val KEY_PRICE_MIN = "price_min"
        private const val KEY_PRICE_MAX = "price_max"
        private const val KEY_DATETIME = "datetime"
        private const val KEY_EMAIL = "email"

        // Params

        private const val PARAM_FROM_CINEMAS = "from_cinemas"
        private const val PARAM_FROM_SETTINGS = "from_settings"
        private const val PARAM_FROM_RATE_DIALOG = "from_rate_dialog"
        private const val PARAM_ORIGINAL = "original"
        private const val PARAM_RUSSIAN = "russian"
    }

    private val analytics = FirebaseAnalytics.getInstance(context)

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    fun logStartApp(city: String) {
        val bundle = Bundle().apply {
            putString(KEY_CITY, city)
        }
        analytics.logEvent(EVENT_APP, bundle)
    }

    fun logOpenRateDialog() {
        analytics.logEvent(EVENT_RATE_DIALOG, Bundle())
    }

    fun logOpenMain() {
        analytics.logEvent(EVENT_MAIN, Bundle())
    }

    fun logOpenMovies(fromDeepLink: Boolean) {
        val bundle = Bundle().apply {
            putBoolean(KEY_FROM_DEEP_LINK, fromDeepLink)
        }
        analytics.logEvent(EVENT_FILMS, bundle)
    }

    fun logOpenCinemas(fromDeepLink: Boolean) {
        val bundle = Bundle().apply {
            putBoolean(KEY_FROM_DEEP_LINK, fromDeepLink)
        }
        analytics.logEvent(EVENT_CINEMAS, bundle)
    }

    fun logOpenCinemasMap(city: String, fromCinemas: Boolean) {
        val fromValue = if (fromCinemas) PARAM_FROM_CINEMAS else PARAM_FROM_SETTINGS
        val bundle = Bundle().apply {
            putString(KEY_CITY, city)
            putString(KEY_FROM, fromValue)
        }
        analytics.logEvent(EVENT_CINEMAS_MAP, bundle)
    }

    fun logOpenSettings() {
        analytics.logEvent(EVENT_SETTINGS, Bundle())
    }

    fun logOpenMovie(movieId: Long, movieName: String?, fromDeepLink: Boolean) {
        val bundle = Bundle().apply {
            putLong(KEY_MOVIE_ID, movieId)
            putString(KEY_MOVIE_NAME, movieName)
            putBoolean(KEY_FROM_DEEP_LINK, fromDeepLink)
        }
        analytics.logEvent(EVENT_MOVIE, bundle)
    }

    fun logOpenYouTube(movieId: Long, movieName: String, trailerId: String, isOriginal: Boolean) {
        val languageValue = if (isOriginal) PARAM_ORIGINAL else PARAM_RUSSIAN
        val bundle = Bundle().apply {
            putLong(KEY_MOVIE_ID, movieId)
            putString(KEY_MOVIE_NAME, movieName)
            putString(KEY_TRAILER_ID, trailerId)
            putString(KEY_LANGUAGE, languageValue)
        }
        analytics.logEvent(EVENT_YOUTUBE, bundle)
    }

    fun logShareMovie(filmId: Long, filmName: String) {
        val bundle = Bundle().apply {
            putLong(KEY_MOVIE_ID, filmId)
            putString(KEY_MOVIE_NAME, filmName)
        }
        analytics.logEvent(EVENT_SHARE, bundle)
    }

    fun logOpenCinema(cinemaId: Long, cinemaName: String?, fromDeepLink: Boolean) {
        val bundle = Bundle().apply {
            putLong(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putBoolean(KEY_FROM_DEEP_LINK, fromDeepLink)
        }
        analytics.logEvent(EVENT_CINEMA, bundle)
    }

    fun logOpenCinemaFromMap(cinemaId: Long) {
        val bundle = Bundle().apply {
            putLong(KEY_CINEMA_ID, cinemaId)
        }
        analytics.logEvent(EVENT_CINEMA_FROM_MAP, bundle)
    }

    fun logCinemaAddressClick(cinemaId: Long, cinemaName: String, address: String) {
        val bundle = Bundle().apply {
            putLong(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_ADDRESS, address)
        }
        analytics.logEvent(EVENT_CINEMA_ADDRESS, bundle)
    }

    fun logCinemaPhoneClick(cinemaId: Long, cinemaName: String, phone: String) {
        val bundle = Bundle().apply {
            putLong(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_PHONE, phone)
        }
        analytics.logEvent(EVENT_CINEMA_DIALER, bundle)
    }

    fun logCinemaWebClick(cinemaId: Long, cinemaName: String, url: String) {
        val bundle = Bundle().apply {
            putLong(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_WEB, url)
        }
        analytics.logEvent(EVENT_CINEMA_WEB, bundle)
    }

    fun logOpenAbout() {
        analytics.logEvent(EVENT_ABOUT, Bundle())
    }

    fun logOpenPolicy() {
        analytics.logEvent(EVENT_POLICY, Bundle())
    }

    fun logOpenSocialNetwork(socialNetwork: String, city: String, url: String) {
        val bundle = Bundle().apply {
            putString(KEY_SOCIAL_NETWORK, socialNetwork)
            putString(KEY_CITY, city)
            putString(KEY_SOCIAL_NETWORK_URL, url)
        }
        analytics.logEvent(EVENT_SOCIAL_NETWORK, bundle)
    }

    fun logOpenEmail(email: String) {
        val bundle = Bundle().apply {
            putString(KEY_EMAIL, email)
        }
        analytics.logEvent(EVENT_EMAIL, bundle)
    }

    fun logOpenCityPicker() {
        analytics.logEvent(EVENT_CITY_PICKER, Bundle())
    }

    fun logThemePicker() {
        analytics.logEvent(EVENT_THEME_PICKER, Bundle())
    }

    fun logLanguagePicker() {
        analytics.logEvent(EVENT_LANGUAGE_PICKER, Bundle())
    }

    fun logSwitchCity(city: String) {
        val bundle = Bundle().apply {
            putString(KEY_CITY, city)
        }
        analytics.logEvent(EVENT_SWITCH_CITY, bundle)
    }

    fun logBuyTicket(screening: Screening) {
        val bundle = Bundle().apply {
            putLong(KEY_SCREENING_ID, screening.id)
            putLong(KEY_CINEMA_ID, screening.cinemaId)
            putLong(KEY_MOVIE_ID, screening.movieId)
            putInt(KEY_PRICE_MIN, screening.priceMin)
            putInt(KEY_PRICE_MAX, screening.priceMax)
            putString(KEY_DATETIME, screening.dateTime.toString("dd-MM-yyyy HH:mm"))
        }
        analytics.logEvent(EVENT_BUY_TICKET, bundle)
    }

    fun logOpenPlayStore(fromRateDialog: Boolean) {
        val bundle = Bundle().apply {
            putString(KEY_FROM, if (fromRateDialog) PARAM_FROM_RATE_DIALOG else PARAM_FROM_SETTINGS)
        }
        analytics.logEvent(EVENT_OPEN_PLAY_STORE, bundle)
    }

    fun logOpenGitHub() {
        analytics.logEvent(EVENT_OPEN_GIT_HUB, Bundle())
    }

    fun logActivity(activity: Activity) {
        analytics.setCurrentScreen(activity, activity.javaClass.name.toString(), null)
    }
}