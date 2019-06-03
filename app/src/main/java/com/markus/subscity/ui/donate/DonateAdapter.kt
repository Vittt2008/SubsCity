package com.markus.subscity.ui.donate

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anjlab.android.iab.v3.SkuDetails
import com.markus.subscity.R

/**
 * @author Vitaliy Markus
 */
class DonateAdapter(private val details: List<SkuDetails>,
                    private val clickListener: (SkuDetails) -> Unit) : RecyclerView.Adapter<DonateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_donate, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(details[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var sku: SkuDetails
        private val title = view.findViewById<TextView>(R.id.tv_title)
        private val price = view.findViewById<TextView>(R.id.tv_price)

        init {
            view.setOnClickListener { clickListener.invoke(sku) }
        }

        fun bind(sku: SkuDetails) {
            this.sku = sku
            title.text = sku.description
            price.text = sku.priceText
        }
    }
}