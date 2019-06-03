package com.markus.subscity.ui.cinemasmap

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.ui.cinema.CinemaFragment

/**
 * @author Vitaliy Markus
 */
class CinemaFragmentAdapter(fragmentManager: FragmentManager,
                            private val cinemas: List<Cinema>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = CinemaFragment.withoutToolbar(cinemas[position].id)

    override fun getCount() = cinemas.size
}