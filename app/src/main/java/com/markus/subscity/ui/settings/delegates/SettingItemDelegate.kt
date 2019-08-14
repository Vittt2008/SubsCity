package com.markus.subscity.ui.settings.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R
import com.markus.subscity.ui.settings.Setting

/**
 * @author Vitaliy Markus
 */
class SettingItemDelegate(private val clickListener: (Int) -> Unit) : AbsListItemAdapterDelegate<Setting.Item, Setting, SettingItemDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(item)
    }

    override fun isForViewType(item: Setting, items: MutableList<Setting>, position: Int): Boolean {
        return item is Setting.Item
    }

    override fun onBindViewHolder(item: Setting.Item, viewHolder: ViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: Setting

        private val settingIcon = view.findViewById<ImageView>(R.id.iv_setting_icon)
        private val settingTitle = view.findViewById<TextView>(R.id.tv_setting_title)

        init {
            view.setOnClickListener { clickListener.invoke(item.id) }
        }

        fun bind(item: Setting.Item) {
            this.item = item
            settingIcon.setImageResource(item.icon)
            settingTitle.setText(item.title)
        }
    }
}