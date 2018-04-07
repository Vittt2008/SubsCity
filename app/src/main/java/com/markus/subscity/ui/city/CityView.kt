package com.markus.subscity.ui.city

import com.arellomobile.mvp.MvpView
import com.markus.subscity.api.entities.City

/**
 * @author Vitaliy Markus
 */
interface CityView : MvpView {
    fun showCities(cities: List<City>, currentCity: String)
    fun onError(throwable: Throwable)
}