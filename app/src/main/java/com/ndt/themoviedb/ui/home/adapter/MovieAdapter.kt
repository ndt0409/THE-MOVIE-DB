package com.ndt.themoviedb.ui.home.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.databinding.ItemMovieBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.ui.utils.GetImageAsyncTask
import com.ndt.themoviedb.ui.utils.OnFetchImageListener
import com.ndt.themoviedb.ui.utils.constant.UrlConstant

class MovieAdapter(var onItemClick: (Movie, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<Movie, MovieAdapter.ViewHolder>() {

    private var binding: ItemMovieBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!, onItemClick)
    }

    class ViewHolder(
        private val binding: ItemMovieBinding,
        onItemClick: (Movie, Int) -> Unit
    ) : BaseViewHolder<Movie>(binding, onItemClick) {

        override fun onBindData(itemData: Movie) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            binding.imdbTextView.text = itemData.voteAverage.toString()
        }

        private fun getImageCircle(movie: Movie?) {
            GetImageAsyncTask(
                object : OnFetchImageListener {

                    override fun onImageError(e: Exception?) {
                        e?.printStackTrace()
                    }

                    override fun onImageLoaded(bitmap: Bitmap?) {
                        bitmap?.let { binding.itemMovieImageView.setImageBitmap(bitmap) }
                    }
                }).execute(UrlConstant.BASE_URL_IMAGE + movie?.posterPath)
        }
    }
}

