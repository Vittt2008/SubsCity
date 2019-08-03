package com.markus.subscity.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @author Vitaliy Markus
 */
sealed class Setting(val id: Int, @DrawableRes val icon: Int, @StringRes val title: Int) {

    class Item(id: Int, @DrawableRes icon: Int, @StringRes title: Int) : Setting(id, icon, title)
    class TwoLineItem(id: Int, @DrawableRes icon: Int, @StringRes title: Int, val subtitle: String) : Setting(id, icon, title)
    class ThemeItem(id: Int, @DrawableRes icon: Int, @StringRes title: Int) : Setting(id, icon, title)
}