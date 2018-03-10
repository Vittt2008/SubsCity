package com.source.subscity.ui.settings

import com.arellomobile.mvp.MvpView

/**
 * @author Vitaliy Markus
 */
interface SettingsView : MvpView {
    fun showSettings(city: String)
}