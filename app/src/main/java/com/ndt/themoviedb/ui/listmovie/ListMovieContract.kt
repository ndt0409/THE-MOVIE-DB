package com.ndt.themoviedb.ui.listmovie

import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.MovieResultPage
import com.ndt.themoviedb.ui.base.BasePresenter
import com.ndt.themoviedb.utils.constant.UrlConstant


interface ListMovieContract {

    interface View {
        fun onGetMoviesSuccess(movies: List<Movie>)
        fun onGetMovieResultPage(movieResultPage: MovieResultPage)
        fun onError(exception: Exception?)
        fun onLoading(isLoad: Boolean)
    }

    interface Presenter : BasePresenter<View?> {
        fun getMovies(
            type: String,
            query: String = UrlConstant.BASE_QUERY_DEFAULT,
            page: Int = UrlConstant.BASE_PAGE_DEFAULT
        )
    }
}
