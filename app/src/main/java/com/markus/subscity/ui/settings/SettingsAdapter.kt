package com.markus.subscity.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class SettingsAdapter(private val settings: List<SettingsView.SettingItem>,
                      private val clickListener: (Int) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    companion object {
        private const val ONE_LINE_TYPE = R.layout.item_setting
        private const val TWO_LINE_TYPE = R.layout.item_setting_2_lines
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(settings[position])
    }

    override fun getItemCount(): Int {
        return settings.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (settings[position].subtitle == null) ONE_LINE_TYPE else TWO_LINE_TYPE
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: SettingsView.SettingItem

        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)

        init {
            view.setOnClickListener { clickListener.invoke(item.id) }
        }

        fun bind(item: SettingsView.SettingItem) {
            this.item = item

            settingIcon.setImageResource(item.icon)
            settingTitle.setText(item.title)
            if (item.subtitle != null) {
                val subtitleView = itemView.findViewById<TextView>(R.id.tv_settings_subtitle)
                subtitleView.text = item.subtitle
            }
        }
    }
}