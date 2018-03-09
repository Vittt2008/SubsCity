package com.source.subscity.ui.movie

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
class MovieScreeningAdapter(private val screenings: List<Screening>) : RecyclerView.Adapter<MovieScreeningAdapter.ViewHolder>() {

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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val time = view.findViewById<TextView>(R.id.tv_screening_time)
        private val price = view.findViewById<TextView>(R.id.tv_screening_time)

        fun bind(screening: Screening) {
            time.text = screening.dateTime.toString()
            price.text = screening.priceMin.toString()
        }
    }
}