package com.source.subscity.ui.settings

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.source.subscity.R

/**
 * @author Vitaliy Markus
 */
class SettingsAdapter(city: String, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    companion object {
        const val SOON_AT_BOX_OFFICE = 0
        const val CINEMA_MAP = 1
        const val SALES = 2
        const val ABOUT = 3
        const val CITY = 4
        const val DONATE = 5
    }

    private val settings = listOf(
            //SettingItem(SOON_AT_BOX_OFFICE, R.drawable.ic_menu_soom_at_box_office, R.string.setting_new_in_cinema_title),
            SettingItem(CINEMA_MAP, R.drawable.ic_menu_map, R.string.setting_cinema_map_title),
            //SettingItem(SALES, R.drawable.ic_menu_sales, R.string.setting_sale_title),
            SettingItem(ABOUT, R.drawable.ic_menu_about, R.string.setting_about_title),
            //SettingItem(DONATE, R.drawable.ic_menu_donate, R.string.setting_donate_title),
            SettingItem(CITY, R.drawable.ic_menu_city, R.string.setting_city_title, city))

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

        private lateinit var item: SettingItem

        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)
        private val settingCity = view.findViewById<TextView>(R.id.tv_settings_city)

        init {
            view.setOnClickListener { clickListener.invoke(item.id) }
        }

        fun bind(item: SettingItem) {
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

    class SettingItem(val id: Int, @DrawableRes val icon: Int, @StringRes val title: Int, val city: String = "")
}