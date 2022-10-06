package com.ndt.themoviedb.ui.details

import android.graphics.Bitmap
import android.widget.ImageView
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.ui.utils.GetImageAsyncTask
import com.ndt.themoviedb.ui.utils.OnFetchImageListener
import com.ndt.themoviedb.ui.utils.constant.UrlConstant

class MovieDetailsPresenter(private val movieRepository: MovieRepository) :
    MovieDetailsContract.Presenter {
    private var view: MovieDetailsContract.View? = null

    override fun onStart() {
        //TODO something
    }

    override fun onStop() {
        //TODO something
    }

    override fun setView(view: MovieDetailsContract.View?) {
        this.view = view
    }

    override fun getMovieDetails(movieID: Int) {
        view?.onLoading(false)
        movieRepository.getMovieDetails(
            movieID,
            object : OnDataLoadedCallback<MovieDetailsResponse> {
                override fun onError(e: Exception) {
                    view?.onError(e)
                }

                override fun onSuccess(data: MovieDetailsResponse?) {
                    data ?: return
                    view?.run {
                        onGetCastsSuccess(data.casts)
                        onGetMovieSuccess(data.movies)
                        onGetProducesSuccess(data.produce)
                        onGetGenresSuccess(data.genres)
                    }
                    view?.onLoading(true)
                }
            })
    }

    override fun getImageAsync(strUrl: String?, imageView: ImageView) {
        GetImageAsyncTask(
            object : OnFetchImageListener {

                override fun onImageError(e: Exception?) {
                    e?.printStackTrace()
                }

                override fun onImageLoaded(bitmap: Bitmap?) {
                    bitmap?.let { imageView.setImageBitmap(bitmap) }
                }
            }).execute(UrlConstant.BASE_URL_IMAGE + strUrl)
    }
}
