package com.source.subscity.ui.settings

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.source.subscity.R

/**
 * @author Vitaliy Markus
 */
class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    private val settings = listOf(
            SettingItem(R.drawable.ic_movie_star, R.string.setting_new_in_cinema_title),
            SettingItem(R.drawable.ic_movie_star, R.string.setting_cinema_map_title),
            SettingItem(R.drawable.ic_movie_star, R.string.setting_sale_title),
            SettingItem(R.drawable.ic_movie_star, R.string.setting_about_title),
            SettingItem(R.drawable.ic_movie_star, R.string.setting_city_title, "Санкт-Петербург"))

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
        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)
        private val settingCity = view.findViewById<TextView>(R.id.tv_settings_city)

        init {
            view.setOnClickListener { Toast.makeText(view.context, "CLICK", Toast.LENGTH_SHORT).show() }
        }

        fun bind(item: SettingItem) {
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

    class SettingItem(@DrawableRes val icon: Int, @StringRes val title: Int, val city: String = "")
}