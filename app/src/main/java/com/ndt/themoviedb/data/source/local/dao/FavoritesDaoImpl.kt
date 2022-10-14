package com.ndt.themoviedb.data.source.local.dao

import android.content.ContentValues
import android.content.Context
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.model.Favorite.MovieEntry
import com.ndt.themoviedb.data.source.local.DatabaseHelper

class FavoritesDaoImpl(context: Context) : FavoritesDao {
    private val database = DatabaseHelper.getInstance(context).writableDatabase

    override fun getAllFavorites(): MutableList<Favorite> {
        val cursor = database.query(
            MovieEntry.FAVORITE,
            null,
            null,
            null,
            null,
            null,
            null
        )
            .apply { moveToFirst() }
        return mutableListOf<Favorite>().apply {
            while (!cursor.isAfterLast) {
                add(Favorite(cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
    }

    override fun addFavorite(favorite: Favorite): Boolean {
        if (findFavorite(favorite.id)) return false
        val contentValues = ContentValues().apply {
            put(MovieEntry.ID, favorite.id)
            put(MovieEntry.TITLE, favorite.title)
            put(MovieEntry.OVERVIEW, favorite.overview)
            put(MovieEntry.POSTER_PATH, favorite.posterPath)
            put(MovieEntry.VOTE_AVERAGE, favorite.voteAverage)
            put(MovieEntry.RELEASE_DATE, favorite.releaseDate)
        }
        return database.insert(MovieEntry.FAVORITE, null, contentValues) > 0
    }

    override fun deleteFavorite(movieId: String): Boolean {
        val selectionArgs = arrayOf(movieId)
        return database.delete(MovieEntry.FAVORITE, MovieEntry.SELECTION_LIKE_ID, selectionArgs) > 0
    }

    override fun findFavorite(movieId: String): Boolean {
        val selectionArgs = arrayOf(movieId)
        val cursor = database.query(
            MovieEntry.FAVORITE,
            null,
            MovieEntry.SELECTION_LIKE_ID,
            selectionArgs,
            null,
            null,
            null
        )
        return (cursor.count > 0).also { cursor.close() }
    }

    companion object {
        private var instance: FavoritesDaoImpl? = null
        fun getInstance(context: Context): FavoritesDaoImpl =
            instance ?: FavoritesDaoImpl(context).also { instance = it }
    }
}
