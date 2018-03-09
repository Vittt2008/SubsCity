package com.source.subscity.ui.cinemas

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.MetroProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemasAdapter(private val cinemas: List<Cinema>) : RecyclerView.Adapter<CinemasAdapter.ViewHolder>() {

    @Inject
    lateinit var metroProvider: MetroProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return cinemas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cinemas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cinemaName = view.findViewById<TextView>(R.id.tv_cinema_name)
        private val cinemaAddress = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val cinemaMetroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)

        init {
            view.setOnClickListener { Toast.makeText(view.context, "CLICK", Toast.LENGTH_SHORT).show() }
        }

        fun bind(cinema: Cinema) {
            cinemaName.text = cinema.name
            cinemaAddress.text = cinema.location.address
            cinemaMetroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
        }

    }
}