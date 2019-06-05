package com.markus.subscity.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.R
import com.markus.subscity.providers.BillingProvider
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.PreferencesProvider
import com.markus.subscity.ui.settings.SettingsView.Companion.ABOUT
import com.markus.subscity.ui.settings.SettingsView.Companion.CINEMA_MAP
import com.markus.subscity.ui.settings.SettingsView.Companion.CITY
import com.markus.subscity.ui.settings.SettingsView.Companion.THEME
import com.markus.subscity.ui.settings.SettingsView.Companion.DONATE
import com.markus.subscity.ui.settings.SettingsView.Companion.LANGUAGE
import com.markus.subscity.ui.settings.SettingsView.Companion.POLICY
import com.markus.subscity.ui.settings.SettingsView.Companion.RATE_APP
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SettingsPresenter @Inject constructor(private val cityProvider: CityProvider,
                                            private val preferencesProvider: PreferencesProvider,
                                            private val billingProvider: BillingProvider) : MvpPresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        cityProvider.asyncCity
                .observeOn(AndroidSchedulers.mainThread())
                .map { createSettings(cityProvider.cityName) }
                .subscribe { settings -> viewState.showSettings(settings) }

    }

    fun showCinemasMap() {
        viewState.showCinemasMap(cityProvider.city)
    }

    fun openPlayStore() {
        preferencesProvider.getAppPreferences().edit().putBoolean(PreferencesProvider.WAS_RANKED_KEY, true).apply()
        viewState.openPlayStore()
    }

    private fun createSettings(cityName: String): List<SettingsView.SettingItem> {
        val settings = mutableListOf(
                SettingsView.SettingItem(CINEMA_MAP, R.drawable.ic_menu_map, R.string.setting_cinema_map_title),
                SettingsView.SettingItem(THEME, R.drawable.ic_menu_theme, R.string.setting_theme),
                SettingsView.SettingItem(LANGUAGE, R.drawable.ic_menu_language, R.string.setting_language),
                SettingsView.SettingItem(RATE_APP, R.drawable.ic_menu_rate_app, R.string.setting_rate_app),
                SettingsView.SettingItem(ABOUT, R.drawable.ic_menu_about, R.string.setting_about_title),
                SettingsView.SettingItem(POLICY, R.drawable.ic_menu_policy, R.string.setting_policy_title),
                SettingsView.SettingItem(CITY, R.drawable.ic_menu_city, R.string.setting_city_title, cityName))
        if (billingProvider.isBillingAvailable) {
            settings.add(SettingsView.SettingItem(DONATE, R.drawable.ic_menu_donate, R.string.setting_donate_title))
        }
        return settings
    }
}