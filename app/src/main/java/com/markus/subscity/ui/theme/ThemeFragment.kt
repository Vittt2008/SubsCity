package com.markus.subscity.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.markus.subscity.R
import com.markus.subscity.extensions.setSupportActionBar

/**
 * @author Vitaliy Markus
 */
class ThemeFragment : Fragment() {

    private lateinit var themeList: RecyclerView

    companion object {
        fun newInstance(): ThemeFragment {
            return ThemeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_theme, container, false)
        themeList = root.findViewById(R.id.rv_list)
        setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = createThemeList()
        themeList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ThemeAdapter(requireActivity(), items, ::updateTheme)
        }
    }

    private fun createThemeList(): List<SelectedThemeItem> {
        val tempNightMode = AppCompatDelegate.getDefaultNightMode()
        val nightMode = if (tempNightMode == MODE_NIGHT_UNSPECIFIED) MODE_NIGHT_NO else tempNightMode
        val lastItem = if (BuildCompat.isAtLeastQ()) {
            SelectedThemeItem(MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_system_default, MODE_NIGHT_FOLLOW_SYSTEM == nightMode)
        } else {
            SelectedThemeItem(MODE_NIGHT_AUTO_BATTERY, R.string.theme_by_battery_saver, MODE_NIGHT_AUTO_BATTERY == nightMode)
        }
        return listOf(
                SelectedThemeItem(MODE_NIGHT_NO, R.string.theme_light, MODE_NIGHT_NO == nightMode),
                SelectedThemeItem(MODE_NIGHT_YES, R.string.theme_dark, MODE_NIGHT_YES == nightMode),
                lastItem
        )
    }

    private fun updateTheme(themeItem: SelectedThemeItem) {
        AppCompatDelegate.setDefaultNightMode(themeItem.mode)
    }
}