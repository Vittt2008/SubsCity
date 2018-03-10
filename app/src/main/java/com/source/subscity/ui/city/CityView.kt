package com.source.subscity.ui.city

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.City

/**
 * @author Vitaliy Markus
 */
interface CityView : MvpView {
    fun showCities(cities: List<City>)
    fun onError(throwable: Throwable)
}