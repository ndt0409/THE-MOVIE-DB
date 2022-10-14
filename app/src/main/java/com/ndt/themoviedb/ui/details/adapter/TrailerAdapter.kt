package com.ndt.themoviedb.ui.details.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.MovieTrailer
import com.ndt.themoviedb.databinding.ItemTrailerBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.utils.GetImageAsyncTask
import com.ndt.themoviedb.utils.OnFetchImageListener
import com.ndt.themoviedb.utils.constant.UrlConstant

class TrailerAdapter(var onItemClick: (MovieTrailer, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<MovieTrailer, TrailerAdapter.ViewHolder>() {

    private var binding: ItemTrailerBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        binding =
            ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!, onItemClick)
    }

    class ViewHolder(
        private val binding: ItemTrailerBinding,
        onItemClick: (MovieTrailer, Int) -> Unit
    ) : BaseViewHolder<MovieTrailer>(binding, onItemClick) {

        override fun onBindData(itemData: MovieTrailer) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            binding.textTrailer.text = itemData.trailerName
        }

        private fun getImageCircle(movieTrailer: MovieTrailer?) {
            GetImageAsyncTask(
                object : OnFetchImageListener {

                    override fun onImageError(e: Exception?) {
                        e?.printStackTrace()
                    }

                    override fun onImageLoaded(bitmap: Bitmap?) {
                        bitmap?.let { binding.imageTrailer.setImageBitmap(bitmap) }
                    }
                }).execute(UrlConstant.BASE_URL_IMAGE_2 + movieTrailer?.trailerKey + UrlConstant.BASE_URL_IMAGE_DEFAULT)
        }
    }
}
