package com.ndt.themoviedb.ui.details.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.databinding.ItemCastBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.utils.GetImageAsyncTask
import com.ndt.themoviedb.utils.OnFetchImageListener
import com.ndt.themoviedb.utils.constant.UrlConstant

class CastAdapter(var onItemClick: (Cast, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<Cast, CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    class ViewHolder(
        private val binding: ItemCastBinding, onItemClick: (Cast, Int) -> Unit
    ) : BaseViewHolder<Cast>(binding, onItemClick) {

        override fun onBindData(itemData: Cast) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            binding.textCastName.text = itemData.name
        }

        private fun getImageCircle(cast: Cast?) {
            GetImageAsyncTask(object : OnFetchImageListener {

                override fun onImageError(e: Exception?) {
                    e?.printStackTrace()
                }

                override fun onImageLoaded(bitmap: Bitmap?) {
                    bitmap?.let { binding.imageCastProfile.setImageBitmap(bitmap) }
                }
            }).execute(UrlConstant.BASE_URL_IMAGE + cast?.profilePath)
        }
    }
}
