package com.markus.subscity.ui.cinema

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.*
import com.markus.subscity.ui.movie.MovieActivity
import com.markus.subscity.utils.IntentUtils

/**
 * @author Vitaliy Markus
 */
open class CinemaFragment : MvpAppCompatFragment(), CinemaView {

    @InjectPresenter
    lateinit var cinemaPresenter: CinemaPresenter

    private lateinit var cinemaInfoList: RecyclerView
    private var toolbar: Toolbar? = null

    private var adapter: CinemaAdapterDelegates? = null

    companion object {

        private const val EXTRA_LAYOUT_ID = "layout_id"
        private const val EXTRA_SHOW_NAME = "show_name"

        fun withToolbar(cinemaId: Long) = newInstance(cinemaId, R.layout.fragment_cinema_toolbar, false)

        fun withoutToolbar(cinemaId: Long) = newInstance(cinemaId, R.layout.fragment_cinema, true)

        private fun newInstance(cinemaId: Long, @LayoutRes layoutId: Int, showCinemaName: Boolean): CinemaFragment {
            return CinemaFragment().apply {
                arguments = Bundle().apply {
                    putLong(CinemaActivity.EXTRA_CINEMA_ID, cinemaId)
                    putInt(EXTRA_LAYOUT_ID, layoutId)
                    putBoolean(EXTRA_SHOW_NAME, showCinemaName)
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
        val root = inflater.inflate(arguments!!.getInt(EXTRA_LAYOUT_ID), container, false)
        cinemaInfoList = root.findViewById(R.id.rv_list)
        cinemaInfoList.layoutManager = LinearLayoutManager(activity)
        toolbar = root.findViewById(R.id.toolbar)
        toolbar?.let(::setSupportActionBar)
        return root
    }

    override fun showCinema(cinema: Cinema, second: List<CinemaPresenter.MovieScreenings>) {
        if (adapter == null) {
            toolbar?.title = cinema.name
            adapter = CinemaAdapterDelegates(cinema, second, arguments!!.getBoolean(EXTRA_SHOW_NAME), ::openMap, ::call, ::openSite, ::buyTicket, ::openMovie)
            cinemaInfoList.adapter = adapter
        } else {
            adapter!!.updateScreenings(second)
        }
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
        adapter?.updateScreenings(emptyList())
    }

    override fun openMovie(movie: Movie) {
        analytics().logOpenMovie(movie.id, movie.title.russian, false)
        MovieActivity.start(activity!!, movie.id)
    }

    private fun openMap(cinema: Cinema) {
        analytics().logCinemaAddressClick(cinema.id, cinema.name, cinema.location.address)
        val geoUri = getString(R.string.geo_uri, cinema.location.latitude, cinema.location.longitude, Uri.encode(cinema.name))
        val uri = Uri.parse(geoUri)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        openIntent(intent, R.string.cinema_no_map_application)
    }

    private fun call(cinema: Cinema) {
        val phone = cinema.phones.first()
        analytics().logCinemaPhoneClick(cinema.id, cinema.name, phone)
        val intent = IntentUtils.createOpenDialerIntent(phone)
        openIntent(intent, R.string.cinema_no_phone_application)
    }

    private fun openSite(cinema: Cinema) {
        val url = cinema.urls.first()
        analytics().logCinemaWebClick(cinema.id, cinema.name, url)
        openUrl(Uri.parse(url))
    }

    private fun buyTicket(screening: Screening) {
        analytics().logBuyTicket(screening)
        openUrl(Uri.parse(screening.ticketsUrl))
    }

}