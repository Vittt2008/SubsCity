package com.source.subscity.ui.cinema

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.MetroProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaAdapter(private val cinema: Cinema,
                    private val mapClickListener: (Cinema) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as InfoViewHolder).bind(cinema)
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val mapButton = view.findViewById<ViewGroup>(R.id.cl_cinema_map)

        init {
            mapButton.setOnClickListener { mapClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
        }

    }
}