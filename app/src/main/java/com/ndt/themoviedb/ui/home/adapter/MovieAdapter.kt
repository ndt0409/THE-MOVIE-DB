package com.ndt.themoviedb.ui.home.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.databinding.ItemMovieBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.ui.utils.GetImageAsyncTask
import com.ndt.themoviedb.ui.utils.OnFetchImageListener
import com.ndt.themoviedb.ui.utils.UrlConstant
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : BaseAdapter<Movie, MovieAdapter.ViewHolder>() {
    var onItemClick: (Movie, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false), onItemClick
        )

    class ViewHolder(
        itemView: View,
        onItemClick: (Movie, Int) -> Unit
    ) : BaseViewHolder<Movie>(itemView, onItemClick) {

        override fun onBindData(itemData: Movie) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            itemView.imdb_text_view.text = itemData.voteAverage.toString()
        }

        private fun getImageCircle(movie: Movie?) {
            GetImageAsyncTask(
                object : OnFetchImageListener {

                    override fun onImageError(e: Exception?) {
                        e?.printStackTrace()
                    }

                    override fun onImageLoaded(bitmap: Bitmap?) {
                        bitmap?.let { itemView.item_movie_image_view.setImageBitmap(bitmap) }
                    }
                }).execute(UrlConstant.BASE_URL_IMAGE + movie?.posterPath)
        }
    }
}
