package com.markus.subscity.ui.theme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class ThemeAdapter(private val context: Context,
                   private val themeItems: List<SelectedThemeItem>,
                   private val clickListener: (SelectedThemeItem) -> Unit) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_picker, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(themeItems[position])
    }

    override fun getItemCount(): Int {
        return themeItems.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: SelectedThemeItem

        private val isSelected = view.findViewById<RadioButton>(R.id.rd_button)
        private val name = view.findViewById<TextView>(R.id.tv_city)

        init {
            view.setOnClickListener {
                themeItems.forEach { it.isSelected = false }
                item.isSelected = true
                notifyItemRangeChanged(0, themeItems.size)
                clickListener.invoke(item)
            }
        }

        fun bind(item: SelectedThemeItem) {
            this.item = item

            isSelected.isChecked = item.isSelected
            name.setText(item.title)
            name.setTextColor(ContextCompat.getColor(context, if (item.isSelected) R.color.title_color else R.color.subtitle_color))
        }

    }
}