package com.source.subscity.ui.city

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.City
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.CityProvider
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CityAdapter(private val context: Context,
                  cities: List<City>,
                  city: String,
                  private val clickListener: (String) -> Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    @Inject
    lateinit var cityProvider: CityProvider
    private val selectableCities: List<SelectableCity>

    init {
        SubsCityDagger.component.inject(this)
        selectableCities = cities.map { SelectableCity(it.id, it.name, it.id == city) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return selectableCities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(selectableCities[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var city: SelectableCity

        private val isSelected = view.findViewById<RadioButton>(R.id.rd_button)
        private val name = view.findViewById<TextView>(R.id.tv_city)

        init {
            view.setOnClickListener {
                selectableCities.forEach { it.isSelected = false }
                city.isSelected = true
                notifyItemRangeChanged(0, selectableCities.size)
                clickListener.invoke(city.id)
            }
        }

        fun bind(city: SelectableCity) {
            this.city = city

            isSelected.isChecked = city.isSelected
            name.text = city.name
            name.setTextColor(ContextCompat.getColor(context, if (city.isSelected) R.color.title_color else R.color.subtitle_color))
        }
    }

    class SelectableCity(val id: String, val name: String, var isSelected: Boolean)
}