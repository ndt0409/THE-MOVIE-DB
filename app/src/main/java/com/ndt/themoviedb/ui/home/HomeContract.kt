package com.ndt.themoviedb.ui.home

import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.ui.base.BasePresenter
import com.ndt.themoviedb.ui.utils.UrlConstant

interface HomeContract {
    interface View {
        fun onGetGenresSuccess(genres: List<Genres>)
        fun onGetMoviesByGenresIDSuccess(movies: List<Movie>)
        fun onError(exception: Exception?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getGenres()
        fun getMovie(
            type: String,
            query: String = UrlConstant.BASE_QUERY_DEFAULT,
            page: Int = UrlConstant.BASE_PAGE_DEFAULT
        )
    }
}