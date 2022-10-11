package com.ndt.themoviedb.data.source.local

import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.source.MovieDataSource
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback

class MovieLocalDataSource private constructor() : MovieDataSource.Local {

    override fun getCategories(listener: OnDataLoadedCallback<List<Category>>) {
        listener.onSuccess(
            listOf(
                Category(Category.CategoryEntry.NOW_PLAYING, R.drawable.now_playing),
                Category(Category.CategoryEntry.UPCOMING, R.drawable.upcoming),
                Category(Category.CategoryEntry.TOP_RATE, R.drawable.toprated),
                Category(Category.CategoryEntry.POPULAR, R.drawable.popular)
            )
        )
    }

    companion object {
        private var instance: MovieLocalDataSource? = null
        fun getInstance() =
            instance ?: MovieLocalDataSource().also { instance = it }
    }
}
