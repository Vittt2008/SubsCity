package com.markus.subscity.ui.cinema.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.MetroProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaInfoDelegate(private val showCinemaName: Boolean,
                         private val mapClickListener: (Cinema) -> Unit,
                         private val phoneClickListener: (Cinema) -> Unit,
                         private val siteClickListener: (Cinema) -> Unit) : AbsListItemAdapterDelegate<Cinema, Any, CinemaInfoDelegate.InfoViewHolder>() {

    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup): InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_info_2, parent, false)
        return InfoViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return position == 0
    }

    override fun onBindViewHolder(item: Cinema, viewHolder: CinemaInfoDelegate.InfoViewHolder, payloads: List<Any>) {
        viewHolder.bind(item)
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val name = view.findViewById<TextView>(R.id.tv_cinema_name)
        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val phoneNumber = view.findViewById<TextView>(R.id.tv_phone_number)
        private val webSite = view.findViewById<TextView>(R.id.tv_web_site)
        private val phoneNumberLayout = view.findViewById<View>(R.id.cl_call)
        private val webSiteLayout = view.findViewById<View>(R.id.cl_web)


        init {
            view.findViewById<View>(R.id.cl_show_on_map).setOnClickListener { mapClickListener.invoke(cinema) }
            phoneNumberLayout.setOnClickListener { phoneClickListener.invoke(cinema) }
            webSiteLayout.setOnClickListener { siteClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            if (showCinemaName) {
                name.text = cinema.name
                name.visibility = View.VISIBLE
            } else {
                name.visibility = View.GONE
            }
            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)

            val phone = cinema.phones.firstOrNull()
            phoneNumber.visibility = if (phone != null) View.VISIBLE else View.GONE
            phoneNumber.text = phone

            val url = cinema.urls.firstOrNull()
            webSite.visibility = if (phone != null) View.VISIBLE else View.GONE
            webSite.text = url
        }
    }

}