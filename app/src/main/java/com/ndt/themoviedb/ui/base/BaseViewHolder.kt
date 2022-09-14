package com.ndt.themoviedb.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T>(itemView: View, private val onItemClick: (T, Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private var itemData: T? = null

    init {
        itemView.setOnClickListener {
            itemData?.let { onItemClickListener(it) }
        }
    }

    open fun onBindData(itemData: T) {
        this.itemData = itemData
    }

    open fun onItemClickListener(itemData: T) = onItemClick(itemData, adapterPosition)
}
