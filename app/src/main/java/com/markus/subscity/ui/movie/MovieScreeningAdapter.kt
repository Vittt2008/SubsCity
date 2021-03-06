package com.markus.subscity.ui.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.markus.subscity.R
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.getWidthScreen
import com.markus.subscity.providers.DisplayLanguageProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class MovieScreeningAdapter(private val context: Context,
                            private val screenings: List<Screening>,
                            spanCount: Int,
                            private val clickListener: (Screening) -> Unit) : RecyclerView.Adapter<MovieScreeningAdapter.ViewHolder>() {

    private val screenWidth: Int = context.getWidthScreen()
    private val itemHeight = context.resources.getDimensionPixelSize(R.dimen.screenings_size)
    private val itemWidth: Int

    @Inject
    lateinit var displayLanguageProvider: DisplayLanguageProvider

    init {
        SubsCityDagger.component.inject(this)
        val recyclerWidth = screenWidth - 2 * context.resources.getDimensionPixelSize(R.dimen.screenings_padding)
        itemWidth = (recyclerWidth - (spanCount - 1) * context.resources.getDimensionPixelSize(R.dimen.screening_horizontal_margin)) / spanCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_screening, parent, false)
        root.layoutParams = RecyclerView.LayoutParams(itemWidth, itemHeight)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return screenings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(screenings[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var screening: Screening

        private val date = view.findViewById<TextView>(R.id.tv_screening_date)
        private val time = view.findViewById<TextView>(R.id.tv_screening_time)
        private val price = view.findViewById<TextView>(R.id.tv_screening_price)

        init {
            view.setOnClickListener { clickListener.invoke(screening) }
        }

        fun bind(screening: Screening) {
            this.screening = screening

            date.text = screening.dateTime.toString("d MMM", displayLanguageProvider.displayLanguage).replace(".", "")
            time.text = screening.dateTime.toString("HH:mm")
            val ticketPrice = if (screening.priceMin != 0) screening.priceMin else if (screening.priceMax != 0) screening.priceMax else 0
            price.text = if (ticketPrice != 0) context.getString(R.string.movie_screening_price, ticketPrice) else "—"
        }
    }
}