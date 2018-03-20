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
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.extensions.*

/**
 * @author Vitaliy Markus
 */
class CinemaFragment : MvpAppCompatFragment(), CinemaView {

    @InjectPresenter
    lateinit var cinemaPresenter: CinemaPresenter

    private lateinit var cinemaInfoList: RecyclerView

    private var adapter: CinemaAdapter? = null

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
        val root = inflater.inflate(R.layout.fragment_cinema, container, false)
        cinemaInfoList = root.findViewById(R.id.rv_list)
        cinemaInfoList.layoutManager = LinearLayoutManager(activity)
        activity!!.setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun showCinema(cinema: Cinema, second: List<CinemaPresenter.MovieScreenings>) {
        if (adapter == null) {
            activity!!.supportActionBar.title = cinema.name
            adapter = CinemaAdapter(cinema, second, ::openMap, ::call, ::openSite, ::buyTicket)
            cinemaInfoList.adapter = adapter
        } else {
            adapter!!.updateScreenings(second)
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

    private fun call(cinema: Cinema) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${cinema.phones.first()}")
        startActivity(intent)
    }

    private fun openSite(cinema: Cinema) {
        openUrl(Uri.parse(cinema.urls.first()))
    }

    private fun buyTicket(screening: Screening) {
        openUrl(Uri.parse(screening.ticketsUrl))
    }

}