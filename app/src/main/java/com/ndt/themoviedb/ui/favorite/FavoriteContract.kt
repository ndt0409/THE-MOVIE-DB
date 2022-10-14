package com.ndt.themoviedb.ui.favorite

import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.ui.base.BasePresenter

interface FavoriteContract {
    interface View {
        fun onGetFavoritesSuccess(favorites: MutableList<Favorite>)
        fun updateFavoritesAfterRemovingItem(position: Int)
        fun notifyDeleteFavorite(type: String)
        fun onError(exception: Exception?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getFavorite()
        fun deleteFavorite(position: Int, favoriteId: String)
    }
}
