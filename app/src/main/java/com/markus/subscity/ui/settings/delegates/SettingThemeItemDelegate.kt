package com.markus.subscity.ui.settings.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.ui.settings.Setting

/**
 * @author Vitaliy Markus
 */
class SettingThemeItemDelegate(private val clickListener: (Boolean) -> Unit) : AbsListItemAdapterDelegate<Setting.ThemeItem, Setting, SettingThemeItemDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_theme, parent, false)
        return ViewHolder(item)
    }

    override fun isForViewType(item: Setting, items: MutableList<Setting>, position: Int): Boolean {
        return item is Setting.ThemeItem
    }

    override fun onBindViewHolder(item: Setting.ThemeItem, viewHolder: ViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: Setting

        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)
        private val settingSwitch = view.findViewById<SwitchCompat>(R.id.sw_setting_checked)

        init {
            view.setOnClickListener { settingSwitch.isChecked = !settingSwitch.isChecked }
            settingSwitch.setOnCheckedChangeListener { _, isChecked -> clickListener.invoke(isChecked) }
        }

        fun bind(item: Setting.ThemeItem) {
            this.item = item
            settingIcon.setImageResource(item.icon)
            settingTitle.setText(item.title)
            settingTitle.setText(item.title)
            settingSwitch.isChecked = item.checked
        }
    }
}
