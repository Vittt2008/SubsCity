package com.markus.subscity.providers

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.os.BuildCompat
import com.markus.subscity.R
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class ThemeProvider @Inject constructor(private val context: Context,
                                        private val preferencesProvider: PreferencesProvider) {

    fun createThemeList(): List<SelectedThemeItem> {
        val themeMode = getCurrentThemeMode()
        val lastItem = createAutoSystemItem(themeMode)
        return listOf(
                SelectedThemeItem(MODE_NIGHT_NO, R.string.theme_light, MODE_NIGHT_NO == themeMode),
                SelectedThemeItem(MODE_NIGHT_YES, R.string.theme_dark, MODE_NIGHT_YES == themeMode),
                lastItem
        )
    }

    fun applyTheme(mode: Int): Boolean {
        return applyThemeMode(mode)
    }

    fun applyTheme(dark: Boolean): Boolean {
        return applyThemeMode(if (dark) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

    fun applyCurrentTheme() {
        val themeMode = getCurrentThemeMode()
        applyThemeMode(themeMode)
    }

    fun getCurrentThemeTitle(): String {
        val mode = getCurrentThemeMode()
        val titleId = createThemeList().find { it.mode == mode }?.title ?: createAutoSystemItem(getAutoSystemValue()).title
        return context.getString(titleId)
    }

    private fun applyThemeMode(mode: Int): Boolean {
        val currentMode = getCurrentThemeMode()
        preferencesProvider.getAppPreferences().edit().putInt(PreferencesProvider.APP_THEME_KEY, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
        return currentMode != mode
    }

    private fun getCurrentThemeMode(): Int {
        return preferencesProvider.getAppPreferences().getInt(PreferencesProvider.APP_THEME_KEY, getAutoSystemValue())
    }

    private fun getAutoSystemValue(): Int {
        return if (BuildCompat.isAtLeastQ()) MODE_NIGHT_FOLLOW_SYSTEM else MODE_NIGHT_AUTO_BATTERY
    }

    private fun createAutoSystemItem(nightMode: Int): SelectedThemeItem {
        return if (BuildCompat.isAtLeastQ()) {
            SelectedThemeItem(MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_system_default, MODE_NIGHT_FOLLOW_SYSTEM == nightMode)
        } else {
            SelectedThemeItem(MODE_NIGHT_AUTO_BATTERY, R.string.theme_by_battery_saver, MODE_NIGHT_AUTO_BATTERY == nightMode)
        }
    }

    class SelectedThemeItem(val mode: Int, @StringRes val title: Int, var isSelected: Boolean)
}