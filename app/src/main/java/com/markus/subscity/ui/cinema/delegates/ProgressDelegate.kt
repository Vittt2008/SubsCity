package com.markus.subscity.ui.cinema.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class ProgressDelegate : AbsListItemAdapterDelegate<Any, Any, ProgressDelegate.ProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
        return ProgressViewHolder(view)
    }

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is ProgressObject
    }

    override fun onBindViewHolder(item: Any, viewHolder: ProgressViewHolder, payloads: List<Any>) {}

    object ProgressObject

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}