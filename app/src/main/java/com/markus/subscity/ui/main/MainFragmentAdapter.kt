package com.markus.subscity.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.markus.subscity.ui.cinemas.CinemasFragment
import com.markus.subscity.ui.movies.MoviesFragment
import com.markus.subscity.ui.settings.SettingsFragment

/**
 * @author Vitaliy Markus
 */
class MainFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val TAB_MAIN_COUNT = 3
    }

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