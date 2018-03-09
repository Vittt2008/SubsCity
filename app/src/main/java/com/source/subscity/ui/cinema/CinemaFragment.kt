package com.source.subscity.ui.cinema

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.dagger.SubsCityDagger

/**
 * @author Vitaliy Markus
 */
class CinemaFragment : MvpAppCompatFragment(), CinemaView {

    @InjectPresenter
    lateinit var cinemaPresenter: CinemaPresenter

    private lateinit var cinemaInfoList: RecyclerView

    companion object {
        fun newInstance(cinemId: Long): CinemaFragment {
            return CinemaFragment().apply {
                arguments = Bundle().apply {
                    putLong(CinemaActivity.EXTRA_CINEMA_ID, cinemId)
                }
            }
        }
    }

    @ProvidePresenter
    fun cinemaPresenter(): CinemaPresenter {
        return SubsCityDagger.component.createCinemaPresenter().apply {
            cinemaId = arguments!!.getLong(CinemaActivity.EXTRA_CINEMA_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cinemaInfoList = inflater.inflate(R.layout.fragment_cinema, container, false) as RecyclerView
        return cinemaInfoList
    }
}