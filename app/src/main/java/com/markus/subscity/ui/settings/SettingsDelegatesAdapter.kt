package com.markus.subscity.ui.settings

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.markus.subscity.ui.settings.delegates.SettingItemDelegate
import com.markus.subscity.ui.settings.delegates.SettingThemeItemDelegate
import com.markus.subscity.ui.settings.delegates.SettingTwoLinesItemDelegate

/**
 * @author Vitaliy Markus
 */
class SettingsDelegatesAdapter(settings: List<Setting>,
                               clickListener: (Int) -> Unit,
                               themeSwitcherListener: (Boolean) -> Unit) : ListDelegationAdapter<List<Setting>>() {

    init {
        delegatesManager
                .addDelegate(SettingItemDelegate(clickListener))
                .addDelegate(SettingTwoLinesItemDelegate(clickListener))
                .addDelegate(SettingThemeItemDelegate(themeSwitcherListener))
        setItems(settings)
    }
}