package com.ndt.themoviedb.ui.listmovie.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.databinding.ItemListMovieBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.ui.utils.GetImageAsyncTask
import com.ndt.themoviedb.ui.utils.OnFetchImageListener
import com.ndt.themoviedb.ui.utils.constant.UrlConstant

class MovieListAdapter(var onItemClick: (Movie, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<Movie, MovieListAdapter.ViewHolder>() {

    private var binding: ItemListMovieBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!, onItemClick)
    }

    class ViewHolder(
        private var binding: ItemListMovieBinding,
        onItemClick: (Movie, Int) -> Unit
    ) : BaseViewHolder<Movie>(binding, onItemClick) {

        override fun onBindData(itemData: Movie) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            binding.voteRatingBar.rating = (itemData.voteAverage * rate).toFloat()
            binding.textViewTitle.text = itemData.title
            binding.overViewText.text = itemData.overView
            binding.textViewReleaseDate.text = itemData.releaseDate
        }

        private fun getImageCircle(movie: Movie?) {
            GetImageAsyncTask(object : OnFetchImageListener {

                override fun onImageError(e: Exception?) {
                    e?.printStackTrace()
                }

                override fun onImageLoaded(bitmap: Bitmap?) {
                    bitmap?.let { binding.imagePoster.setImageBitmap(bitmap) }
                }
            }).execute(UrlConstant.BASE_URL_IMAGE + movie?.posterPath)
        }
    }

    companion object {
        const val rate = 0.5f
    }
}
