package com.markus.subscity.ui.cinema.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class ProgressDelegate : AbsListItemAdapterDelegate<Any, Any, ProgressDelegate.ProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ProgressDelegate.ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
        return ProgressDelegate.ProgressViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return position == 1 && item !is Int
    }

    override fun onBindViewHolder(item: Any, viewHolder: ProgressDelegate.ProgressViewHolder, payloads: List<Any>) {}

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}