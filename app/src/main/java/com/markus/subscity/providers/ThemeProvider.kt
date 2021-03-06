package com.markus.subscity.providers

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import com.markus.subscity.R
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class ThemeProvider @Inject constructor(private val context: Context,
                                        private val preferencesProvider: PreferencesProvider) {

    companion object {
        //Need additional time to finish ripple animation
        private const val THEME_ACTIVATION_DELAY = 150L
    }

    fun createThemeList(): List<SelectedThemeItem> {
        val themeMode = getCurrentThemeMode()
        val lastItem = createAutoSystemItem(themeMode)
        return listOf(
                SelectedThemeItem(AppCompatDelegate.MODE_NIGHT_NO, R.string.theme_light, AppCompatDelegate.MODE_NIGHT_NO == themeMode),
                SelectedThemeItem(AppCompatDelegate.MODE_NIGHT_YES, R.string.theme_dark, AppCompatDelegate.MODE_NIGHT_YES == themeMode),
                lastItem
        )
    }

    fun applyTheme(mode: Int): Single<Boolean> {
        return Completable.timer(THEME_ACTIVATION_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .andThen(Single.fromCallable { applyThemeMode(mode) })
    }

    fun applyTheme(dark: Boolean): Single<Boolean> {
        return Completable.timer(THEME_ACTIVATION_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .andThen(Single.fromCallable { applyThemeMode(if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO) })
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
        return if (BuildCompat.isAtLeastQ()) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }

    private fun createAutoSystemItem(nightMode: Int): SelectedThemeItem {
        return if (BuildCompat.isAtLeastQ()) {
            SelectedThemeItem(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_system_default, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == nightMode)
        } else {
            SelectedThemeItem(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY, R.string.theme_by_battery_saver, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY == nightMode)
        }
    }

    class SelectedThemeItem(val mode: Int, @StringRes val title: Int, var isSelected: Boolean)
}