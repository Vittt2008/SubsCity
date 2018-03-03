package com.source.subscity.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.source.subscity.ui.cinemas.CinemasFragment
import com.source.subscity.ui.movies.MoviesFragment
import com.source.subscity.ui.settings.SettingsFragment

/**
 * @author Vitaliy Markus
 */
class MainFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val TAB_MAIN_COUNT = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MoviesFragment.newInstance()
            1 -> CinemasFragment.newInstance()
            2 -> SettingsFragment.newInstance()
            else -> throw IllegalArgumentException("Position = $position, but it should be less then $TAB_MAIN_COUNT")
        }
    }

    override fun getCount() = TAB_MAIN_COUNT
}