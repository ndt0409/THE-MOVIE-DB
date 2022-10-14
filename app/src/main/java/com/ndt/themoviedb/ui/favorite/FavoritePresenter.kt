package com.ndt.themoviedb.ui.favorite

import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.utils.constant.UrlConstant

class FavoritePresenter(private val movieRepository: MovieRepository) : FavoriteContract.Presenter {
    private var view: FavoriteContract.View? = null

    override fun getFavorite() {
        view?.onLoading(false)
        movieRepository.getFavorites(object : OnDataLoadedCallback<MutableList<Favorite>> {
            override fun onSuccess(data: MutableList<Favorite>?) {
                data ?: return
                view?.onGetFavoritesSuccess(data)
                view?.onLoading(true)
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }

    override fun deleteFavorite(position: Int, favoriteId: String) {
        movieRepository.deleteFavorite(favoriteId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean?) {
                data ?: return
                if (data) {
                    view?.notifyDeleteFavorite(UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS)
                    view?.updateFavoritesAfterRemovingItem(position)
                } else {
                    view?.notifyDeleteFavorite(UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_ERROR)
                }
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: FavoriteContract.View?) {
        this.view = view
    }
}
