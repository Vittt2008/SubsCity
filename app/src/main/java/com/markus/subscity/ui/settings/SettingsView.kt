package com.markus.subscity.ui.settings

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.markus.subscity.api.entities.City

/**
 * @author Vitaliy Markus
 */
interface SettingsView : MvpView {

    companion object {
        const val SOON_AT_BOX_OFFICE = 0
        const val CINEMA_MAP = 1
        const val SALES = 2
        const val ABOUT = 3
        const val CITY = 4
        const val DONATE = 5
        const val RATE_APP = 6
        const val POLICY = 7
    }

    fun showSettings(settings: List<SettingItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCinemasMap(city: City)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openPlayStore()

    class SettingItem(val id: Int, @DrawableRes val icon: Int, @StringRes val title: Int, val city: String = "")
}