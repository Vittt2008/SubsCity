package com.markus.subscity.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.markus.subscity.R
import com.markus.subscity.providers.BillingProvider
import com.markus.subscity.providers.CityProvider
import com.markus.subscity.providers.PreferencesProvider
import com.markus.subscity.providers.ThemeProvider
import com.markus.subscity.ui.settings.SettingsView.Companion.ABOUT
import com.markus.subscity.ui.settings.SettingsView.Companion.CINEMA_MAP
import com.markus.subscity.ui.settings.SettingsView.Companion.CITY
import com.markus.subscity.ui.settings.SettingsView.Companion.DARK_THEME
import com.markus.subscity.ui.settings.SettingsView.Companion.DIALOG_THEME
import com.markus.subscity.ui.settings.SettingsView.Companion.DONATE
import com.markus.subscity.ui.settings.SettingsView.Companion.LANGUAGE
import com.markus.subscity.ui.settings.SettingsView.Companion.POLICY
import com.markus.subscity.ui.settings.SettingsView.Companion.RATE_APP
import com.markus.subscity.ui.settings.SettingsView.Companion.THEME
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
@InjectViewState
class SettingsPresenter @Inject constructor(private val cityProvider: CityProvider,
                                            private val preferencesProvider: PreferencesProvider,
                                            private val themeProvider: ThemeProvider,
                                            private val billingProvider: BillingProvider) : MvpPresenter<SettingsView>() {

    override fun attachView(view: SettingsView) {
        super.attachView(view)
        cityProvider.asyncCity
                .observeOn(AndroidSchedulers.mainThread())
                .map { createSettings(cityProvider.cityName, themeProvider.getCurrentThemeTitle()) }
                .subscribe { settings -> viewState.showSettings(settings) }
    }

    fun showCinemasMap() {
        viewState.showCinemasMap(cityProvider.city)
    }

    fun openPlayStore() {
        preferencesProvider.getAppPreferences().edit().putBoolean(PreferencesProvider.WAS_RANKED_KEY, true).apply()
        viewState.openPlayStore()
    }

    fun openThemeDialog() {
        val themeList = themeProvider.createThemeList()
        viewState.showThemeDialog(themeList)
    }

    fun switchTheme(dark: Boolean) {
        themeProvider.applyTheme(dark)
                .subscribe { recreate ->
                    if (recreate) {
                        viewState.recreate()
                    }
                }
    }

    fun switchTheme(mode: Int) {
        themeProvider.applyTheme(mode)
                .subscribe { recreate ->
                    if (recreate) {
                        viewState.recreate()
                    }
                }
    }

    private fun createSettings(cityName: String, themeTitle: String): List<Setting> {
        val settings = mutableListOf(
                Setting.Item(CINEMA_MAP, R.drawable.ic_menu_map, R.string.setting_cinema_map_title),
                Setting.TwoLineItem(THEME, R.drawable.ic_menu_dark_theme, R.string.setting_theme, themeTitle),
                Setting.ThemeItem(DARK_THEME, R.drawable.ic_menu_dark_theme, R.string.setting_dark_theme),
                Setting.TwoLineItem(DIALOG_THEME, R.drawable.ic_menu_dark_theme, R.string.setting_theme, themeTitle),
                Setting.TwoLineItem(LANGUAGE, R.drawable.ic_menu_language, R.string.setting_language, "Русский"),
                Setting.Item(RATE_APP, R.drawable.ic_menu_rate_app, R.string.setting_rate_app),
                Setting.Item(ABOUT, R.drawable.ic_menu_about, R.string.setting_about_title),
                Setting.Item(POLICY, R.drawable.ic_menu_policy, R.string.setting_policy_title),
                Setting.TwoLineItem(CITY, R.drawable.ic_menu_city, R.string.setting_city_title, cityName))
        if (billingProvider.isBillingAvailable) {
            settings.add(Setting.Item(DONATE, R.drawable.ic_menu_donate, R.string.setting_donate_title))
        }
        return settings
    }
}