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
import com.ndt.themoviedb.ui.home.HomeFragment
import com.ndt.themoviedb.ui.utils.GetImageAsyncTask
import com.ndt.themoviedb.ui.utils.OnClickListener
import com.ndt.themoviedb.ui.utils.OnFetchImageListener
import com.ndt.themoviedb.ui.utils.UrlConstant
import kotlinx.android.synthetic.main.item_slide.view.*

class SliderViewPagerAdapter : PagerAdapter() {
    private val movie = arrayListOf<Movie>()
    private var slideItemClickListener: OnClickListener<Movie>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.item_slide, null)
        slideLayout.youtube_image_view.setOnClickListener {
            slideItemClickListener?.click(
                movie[position]
            )
        }
        getImageSlide(slideLayout.slide_image_view, this.movie[position])
        slideLayout.titleSlideTextView.text = movie[position].title
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

    override fun getCount(): Int = movie.size

    override fun isViewFromObject(view: View, o: Any): Boolean = view === o

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) {
        container.removeView(obj as View)
    }

    fun setSlideItemClickListener(
        movieItemClickListener: HomeFragment
    ) {
        this.slideItemClickListener = movieItemClickListener
    }
}
