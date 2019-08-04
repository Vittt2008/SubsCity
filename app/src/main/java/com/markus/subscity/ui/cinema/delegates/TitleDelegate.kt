package com.markus.subscity.ui.cinema.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class TitleDelegate : AbsListItemAdapterDelegate<TitleDelegate.TitleInfo, Any, TitleDelegate.TitleDividerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): TitleDividerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
        return TitleDividerViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is TitleInfo
    }

    override fun onBindViewHolder(item: TitleInfo, viewHolder: TitleDividerViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    class TitleInfo(val titleId: Int)

    class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(info: TitleInfo) {
            itemView.findViewById<TextView>(R.id.tv_divider_title).setText(info.titleId)
        }
    }
}