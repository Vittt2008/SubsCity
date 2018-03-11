package com.source.subscity.ui.movie

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.screening.Screening

/**
 * @author Vitaliy Markus
 */
class MovieScreeningAdapter(private val context: Context, private val screenings: List<Screening>) : RecyclerView.Adapter<MovieScreeningAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_screening, parent, false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return screenings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(screenings[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.findViewById<TextView>(R.id.tv_screening_date)
        private val time = view.findViewById<TextView>(R.id.tv_screening_time)
        private val price = view.findViewById<TextView>(R.id.tv_screening_price)

        fun bind(screening: Screening) {
            date.text = screening.dateTime.toString("d MMM").replace(".", "")
            time.text = screening.dateTime.toString("HH:mm")
            val ticketPrice = if (screening.priceMin != 0) screening.priceMin else if (screening.priceMax != 0) screening.priceMax else 0
            if (ticketPrice != 0) {
                price.text = context.getString(R.string.movie_screening_price, ticketPrice)
                price.visibility = View.VISIBLE
            } else {
                price.visibility = View.GONE
            }
        }
    }
}