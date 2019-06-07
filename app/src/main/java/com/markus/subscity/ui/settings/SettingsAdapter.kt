package com.markus.subscity.ui.settings

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class SettingsAdapter(private val settings: List<SettingsView.SettingItem>,
                      private val clickListener: (Int) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return settings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(settings[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: SettingsView.SettingItem

        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)
        private val settingCity = view.findViewById<TextView>(R.id.tv_settings_city)

        init {
            view.setOnClickListener { clickListener.invoke(item.id) }
        }

        fun bind(item: SettingsView.SettingItem) {
            this.item = item

            settingIcon.setImageResource(item.icon)
            settingTitle.setText(item.title)
            if (item.city.isEmpty()) {
                settingCity.visibility = View.GONE
            } else {
                settingCity.text = item.city
                settingCity.visibility = View.VISIBLE
            }
        }
    }
}