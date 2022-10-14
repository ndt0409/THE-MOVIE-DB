package com.ndt.themoviedb.ui.details

import android.graphics.Bitmap
import android.widget.ImageView
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.utils.GetImageAsyncTask
import com.ndt.themoviedb.utils.OnFetchImageListener
import com.ndt.themoviedb.utils.constant.UrlConstant

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
                    isFavoriteMovie(movieID.toString())
                }
            })
    }

    private fun isFavoriteMovie(movieID: String) {
        movieRepository.findFavoriteId(movieID, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean?) {
                data ?: return
                view?.showFavoriteImage(
                    if (data) UrlConstant.BASE_NOTIFY_ADD_FAVORITE_SUCCESS
                    else UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS
                )
            }

            override fun onError(e: Exception) {
                view?.onError(e)
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

    override fun handleFavorites(favorite: Favorite) {
        movieRepository.findFavoriteId(favorite.id, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean?) {
                data ?: return
                if (data) deleteFavorite(favorite.id) else addFavorite(favorite)
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }

    private fun addFavorite(favorite: Favorite) {
        movieRepository.addFavorite(favorite, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean?) {
                data ?: return
                view?.run {
                    if (data) {
                        showFavoriteImage(UrlConstant.BASE_NOTIFY_ADD_FAVORITE_SUCCESS)
                        notifyFavorite(UrlConstant.BASE_NOTIFY_ADD_FAVORITE_SUCCESS)
                    } else {
                        notifyFavorite(UrlConstant.BASE_NOTIFY_ADD_FAVORITE_ERROR)
                    }
                }
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }

    private fun deleteFavorite(movieID: String) {
        movieRepository.deleteFavorite(movieID, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean?) {
                data ?: return
                view?.run {
                    if (data) {
                        showFavoriteImage(UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS)
                        notifyFavorite(UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS)
                    } else {
                        notifyFavorite(UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_ERROR)
                    }
                }
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }
}
