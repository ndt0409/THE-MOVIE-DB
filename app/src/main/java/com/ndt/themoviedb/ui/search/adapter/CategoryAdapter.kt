package com.ndt.themoviedb.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.databinding.ItemCategoryBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder

class CategoryAdapter(var onItemClick: (Category, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<Category, CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    class ViewHolder(
        private val binding: ItemCategoryBinding,
        onItemClick: (Category, Int) -> Unit
    ) : BaseViewHolder<Category>(binding, onItemClick) {

        override fun onBindData(itemData: Category) {
            super.onBindData(itemData)
            binding.textCategoryName.text = itemData.categoryName
            binding.imageCategory.setImageResource(itemData.categoryImage)
        }
    }
}
