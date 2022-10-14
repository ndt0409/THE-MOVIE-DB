package com.ndt.themoviedb.ui.favorite.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.databinding.ItemFavoriteBinding
import com.ndt.themoviedb.ui.base.BaseFilterAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder

class FavoriteAdapter(originItems: MutableList<Favorite>) :
    BaseFilterAdapter<Favorite, FavoriteAdapter.ViewHolder>(originItems) {
    var onItemClick: (Favorite, Int) -> Unit = { _, _ -> }
    var onItemDelete: (Favorite, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            onItemClick,
            onItemDelete
        )
    }

    class ViewHolder(
        private val binding: ItemFavoriteBinding,
        onItemClick: (Favorite, Int) -> Unit,
        private val onItemDelete: (Favorite, Int) -> Unit
    ) : BaseViewHolder<Favorite>(binding, onItemClick) {

        override fun onBindData(itemData: Favorite) {
            super.onBindData(itemData)
            itemData.posterPath?.let { convertByteArrayToBitMap(itemData.posterPath) }
            binding.ratingbarVote.rating = itemData.voteAverage.toFloat() * rating
            binding.textViewTitle.text = itemData.title
            binding.textViewOverview.text = itemData.overview
            binding.textViewReleaseDate.text = itemData.releaseDate
        }

        init {
            binding.imageViewDelete.setOnClickListener {
                itemData?.let { favorite -> onItemDelete(favorite, adapterPosition) }
            }
        }

        private fun convertByteArrayToBitMap(byteArray: ByteArray) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imageViewPoster.setImageBitmap(bitmap)
        }
    }
    companion object {
        const val rating = 0.5f
    }
}
