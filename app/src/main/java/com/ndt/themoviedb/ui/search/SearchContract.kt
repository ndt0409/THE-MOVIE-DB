package com.ndt.themoviedb.ui.search

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.ui.base.BasePresenter
import com.ndt.themoviedb.utils.constant.UrlConstant

interface SearchContract {
    interface View {
        fun onGetGenresSuccess(genres: List<Genres>)
        fun getCategoriesSuccess(categories: List<Category>)
        fun onGetMoviesTopRatedSuccess(movies: List<Movie>)
        fun onError(exception: Exception?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getGenres()
        fun getCategories()
        fun getMovies(
            type: String,
            query: String = UrlConstant.BASE_QUERY_DEFAULT,
            page: Int = UrlConstant.BASE_PAGE_DEFAULT
        )
    }
}
