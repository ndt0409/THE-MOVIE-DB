package com.ndt.themoviedb.data.source.local.dao

import com.ndt.themoviedb.data.model.Favorite

interface FavoritesDao {
    fun getAllFavorites(): MutableList<Favorite>
    fun addFavorite(favorite: Favorite): Boolean
    fun deleteFavorite(movieId: String): Boolean
    fun findFavorite(movieId: String): Boolean
}
