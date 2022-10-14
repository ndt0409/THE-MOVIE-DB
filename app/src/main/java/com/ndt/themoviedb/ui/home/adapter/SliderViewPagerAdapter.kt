package com.ndt.themoviedb.ui.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.utils.GetImageAsyncTask
import com.ndt.themoviedb.utils.OnClickListener
import com.ndt.themoviedb.utils.OnFetchImageListener
import com.ndt.themoviedb.utils.constant.UrlConstant
import kotlinx.android.synthetic.main.item_slide.view.*

@Suppress("DEPRECATION")
class SliderViewPagerAdapter : PagerAdapter() {
    private val list = arrayListOf<Movie>()
    private var slideItemClickListener: OnClickListener<Movie>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.item_slide, null)
        slideLayout.youtube_image_view.setOnClickListener {
            slideItemClickListener?.click(
                list[position]
            )
        }
        getImageSlide(slideLayout.slide_image_view, this.list[position])
        slideLayout.text_title_slide.text = list[position].title
        container.addView(slideLayout)
        return slideLayout
    }

    private fun getImageSlide(
        img: ImageView,
        movie: Movie?
    ) {
        GetImageAsyncTask(
            object : OnFetchImageListener {

                override fun onImageError(e: Exception?) {
                    e?.printStackTrace()
                }

                override fun onImageLoaded(bitmap: Bitmap?) {
                    bitmap?.let { img.setImageBitmap(bitmap) }
                }
            }).execute(UrlConstant.BASE_URL_IMAGE + movie?.backdropPath)
    }

    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, o: Any): Boolean = view === o

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) {
        container.removeView(obj as View)
    }

    fun setSlideItemClickListener(
        movieItemClickListener: OnClickListener<Movie>?
    ) {
        this.slideItemClickListener = movieItemClickListener
    }

    fun updateData(newItems: List<Movie>) {
        list.apply {
            if (isNotEmpty()) clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }
}
