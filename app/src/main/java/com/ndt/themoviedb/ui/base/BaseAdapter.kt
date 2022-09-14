package com.ndt.themoviedb.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    private val items = ArrayList<T>()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        getItem(position)?.let { viewHolder.onBindData(it) }
    }

    private fun getItem(position: Int): T? =
        if (position in 0 until itemCount) items[position] else null
}
