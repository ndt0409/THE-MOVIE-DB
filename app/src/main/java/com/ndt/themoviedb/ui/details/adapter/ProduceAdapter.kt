package com.ndt.themoviedb.ui.details.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.databinding.ItemProduceBinding
import com.ndt.themoviedb.ui.base.BaseAdapter
import com.ndt.themoviedb.ui.base.BaseViewHolder
import com.ndt.themoviedb.utils.GetImageAsyncTask
import com.ndt.themoviedb.utils.OnFetchImageListener
import com.ndt.themoviedb.utils.constant.UrlConstant

class ProduceAdapter(var onItemClick: (Produce, Int) -> Unit = { _, _ -> }) :
    BaseAdapter<Produce, ProduceAdapter.ViewHolder>() {

    private var binding: ItemProduceBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemProduceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!, onItemClick)
    }

    inner class ViewHolder(
        private val binding: ItemProduceBinding,
        onItemClick: (Produce, Int) -> Unit
    ) : BaseViewHolder<Produce>(binding, onItemClick) {

        override fun onBindData(itemData: Produce) {
            super.onBindData(itemData)
            getImageCircle(itemData)
            binding.textProduceName.text = itemData.produceName
        }

        private fun getImageCircle(produce: Produce?) {
            GetImageAsyncTask(
                object : OnFetchImageListener {

                    override fun onImageError(e: Exception?) {
                        e?.printStackTrace()
                    }

                    override fun onImageLoaded(bitmap: Bitmap?) {
                        bitmap?.let { binding.imageProduceProfile.setImageBitmap(bitmap) }
                    }
                }).execute(UrlConstant.BASE_URL_IMAGE + produce?.produceLogo)
        }
    }
}
