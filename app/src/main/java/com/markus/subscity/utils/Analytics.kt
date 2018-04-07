package com.markus.subscity.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class Analytics @Inject constructor(context: Context) {

    companion object {

        // Events

        private val EVENT_APP = "event_app"

        private val EVENT_MAIN = "event_main"
        private val EVENT_FILMS = "event_films"
        private val EVENT_CINEMAS = "event_cinemas"
        private val EVENT_CINEMAS_MAP = "event_cinemas_map"
        private val EVENT_SETTINGS = "event_settings"

        private val EVENT_FILM = "event_film"
        private val EVENT_YOUTUBE = "event_youtube"
        private val EVENT_SHARE = "event_share"

        private val EVENT_CINEMA = "event_cinema"
        private val EVENT_CINEMA_ADDRESS = "event_cinema_address"
        private val EVENT_CINEMA_DIALER = "event_cinema_dialer"
        private val EVENT_CINEMA_WEB = "event_cinema_web"

        private val EVENT_ABOUT = "event_about"
        private val EVENT_TELEGRAM = "event_telegram"
        private val EVENT_VKONTAKTE = "event_vkontakte"
        private val EVENT_FACEBOOK = "event_facebook"
        private val EVENT_EMAIL = "event_email"
        private val EVENT_CITY_PICKER = "event_city_chooser"
        private val EVENT_SWITCH_CITY = "event_choose_city"

        private val EVENT_BUY_TICKET = "event_buy_ticket"

        // Keys

        private val KEY_CITY = "city"
        private val KEY_FROM = "from"
        private val KEY_FILM_ID = "film_id"
        private val KEY_FILM_NAME = "film_name"
        private val KEY_TRAILER_ID = "trailer_id"
        private val KEY_CINEMA_ID = "cinema_id"
        private val KEY_CINEMA_NAME = "cinema_name"
        private val KEY_CINEMA_ADDRESS = "cinema_address"
        private val KEY_CINEMA_PHONE = "cinema_phone"
        private val KEY_CINEMA_WEB = "cinema_web"
        private val KEY_TELEGRAM = "telegram"
        private val KEY_VKONTAKTE = "vkontakte"
        private val KEY_FACEBOOK = "facebook"

        // Params

        private val PARAM_FROM_CINEMAS = "from_cinemas"
        private val PARAM_FROM_SETTINGS = "from_settings"
    }

    private val analytics = FirebaseAnalytics.getInstance(context)

    fun startApp(city: String) {
        val bundle = Bundle().apply {
            putString(KEY_CITY, city)
        }
        analytics.logEvent(EVENT_APP, bundle)
    }

    fun openMain() {
        analytics.logEvent(EVENT_MAIN, Bundle())
    }

    fun openFilms() {
        analytics.logEvent(EVENT_FILMS, Bundle())
    }

    fun openCinemas() {
        analytics.logEvent(EVENT_CINEMAS, Bundle())
    }

    fun openCinemasMap(fromCinemas: Boolean) {
        val fromValue = if (fromCinemas) PARAM_FROM_CINEMAS else PARAM_FROM_SETTINGS
        val bundle = Bundle().apply {
            putString(KEY_FROM, fromValue)
        }
        analytics.logEvent(EVENT_CINEMAS_MAP, bundle)
    }

    fun openSettings() {
        analytics.logEvent(EVENT_SETTINGS, Bundle())
    }

    fun openFilm(filmId: String, filmName: String) {
        val bundle = Bundle().apply {
            putString(KEY_FILM_ID, filmId)
            putString(KEY_FILM_NAME, filmName)
        }
        analytics.logEvent(EVENT_FILM, bundle)
    }

    fun openYouTube(filmId: String, filmName: String, trailerId: String) {
        val bundle = Bundle().apply {
            putString(KEY_FILM_ID, filmId)
            putString(KEY_FILM_NAME, filmName)
            putString(KEY_TRAILER_ID, trailerId)
        }
        analytics.logEvent(EVENT_YOUTUBE, bundle)
    }

    fun shareFilm(filmId: String, filmName: String) {
        val bundle = Bundle().apply {
            putString(KEY_FILM_ID, filmId)
            putString(KEY_FILM_NAME, filmName)
        }
        analytics.logEvent(EVENT_SHARE, bundle)
    }

    fun openCinema(cinemaId: String, cinemaName: String) {
        val bundle = Bundle().apply {
            putString(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
        }
        analytics.logEvent(EVENT_CINEMA, bundle)
    }

    fun cinemaAddressClick(cinemaId: String, cinemaName: String, address: String) {
        val bundle = Bundle().apply {
            putString(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_ADDRESS, address)
        }
        analytics.logEvent(EVENT_CINEMA_ADDRESS, bundle)
    }

    fun cinemaPhoneClick(cinemaId: String, cinemaName: String, phone: String) {
        val bundle = Bundle().apply {
            putString(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_PHONE, phone)
        }
        analytics.logEvent(EVENT_CINEMA_DIALER, bundle)
    }

    fun cinemaWebClick(cinemaId: String, cinemaName: String, url: String) {
        val bundle = Bundle().apply {
            putString(KEY_CINEMA_ID, cinemaId)
            putString(KEY_CINEMA_NAME, cinemaName)
            putString(KEY_CINEMA_WEB, url)
        }
        analytics.logEvent(EVENT_CINEMA_WEB, bundle)
    }

    fun openAbout() {
        analytics.logEvent(EVENT_ABOUT, Bundle())
    }

    fun openTelegram(telegram: String) {
        val bundle = Bundle().apply {
            putString(KEY_TELEGRAM, telegram)
        }
        analytics.logEvent(EVENT_TELEGRAM, bundle)
    }

    fun openVkontakte(vkontakte: String) {
        val bundle = Bundle().apply {
            putString(KEY_VKONTAKTE, vkontakte)
        }
        analytics.logEvent(EVENT_VKONTAKTE, bundle)
    }

    fun openFacebook(facebook: String) {
        val bundle = Bundle().apply {
            putString(KEY_FACEBOOK, facebook)
        }
        analytics.logEvent(EVENT_FACEBOOK, bundle)
    }


    fun openEmail() {
        analytics.logEvent(EVENT_EMAIL, Bundle())
    }

    fun openCityPicker() {
        analytics.logEvent(EVENT_CITY_PICKER, Bundle())
    }

    fun switchCity(city: String) {
        val bundle = Bundle().apply {
            putString(KEY_CITY, city)
        }
        analytics.logEvent(EVENT_SWITCH_CITY, bundle)
    }

    fun buyTicket() {
        analytics.logEvent(EVENT_BUY_TICKET, Bundle())
    }
}