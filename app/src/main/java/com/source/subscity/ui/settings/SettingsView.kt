package com.source.subscity.ui.settings

import com.arellomobile.mvp.MvpView
import com.source.subscity.api.entities.City

/**
 * @author Vitaliy Markus
 */
interface SettingsView : MvpView {
    fun showSettings(city: String)
    fun showCinemasMap(city: City)
}