package com.markus.subscity.ui.cinema.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class TitleDelegate : AbsListItemAdapterDelegate<Int, Any, TitleDelegate.TitleDividerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): TitleDelegate.TitleDividerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
        return TitleDividerViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return position == 1 && item is Int
    }

    override fun onBindViewHolder(item: Int, viewHolder: TitleDelegate.TitleDividerViewHolder, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(titleId: Int) {
            itemView.findViewById<TextView>(R.id.tv_divider_title).setText(titleId)
        }
    }
}