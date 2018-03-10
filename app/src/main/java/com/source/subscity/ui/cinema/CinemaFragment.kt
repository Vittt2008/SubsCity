package com.source.subscity.ui.cinema

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.supportActionBar
import com.source.subscity.extensions.toast

/**
 * @author Vitaliy Markus
 */
class CinemaFragment : MvpAppCompatFragment(), CinemaView {

    @InjectPresenter
    lateinit var cinemaPresenter: CinemaPresenter

    private lateinit var cinemaInfoList: RecyclerView

    companion object {
        fun newInstance(cinemaId: Long): CinemaFragment {
            return CinemaFragment().apply {
                arguments = Bundle().apply {
                    putLong(CinemaActivity.EXTRA_CINEMA_ID, cinemaId)
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

    override fun showCinema(cinema: Cinema) {
        activity!!.supportActionBar.title = cinema.name
        cinemaInfoList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CinemaAdapter(cinema, ::openMap)
        }
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    private fun openMap(cinema: Cinema) {
        val geoUri = getString(R.string.geo_uri, cinema.location.latitude, cinema.location.longitude, Uri.encode(cinema.name))
        val uri = Uri.parse(geoUri)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        } else {
            toast(getString(R.string.cinema_no_map_application))
        }
    }

}