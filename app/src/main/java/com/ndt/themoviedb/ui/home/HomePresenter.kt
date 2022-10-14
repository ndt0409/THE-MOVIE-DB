package com.ndt.themoviedb.ui.home

import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.utils.constant.UrlConstant

class HomePresenter(private val movieRepository: MovieRepository) : HomeContract.Presenter {
    private var view: HomeContract.View? = null

    override fun onStart() {
        view?.onLoading(false)
        getGenres()
        getMovie(UrlConstant.BASE_NOW_PLAYING)
        getMovie(UrlConstant.BASE_UPCOMING)
        getMovie(UrlConstant.BASE_POPULAR)
    }

    override fun onStop() {
        //TODO something
    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }

    override
    fun getGenres() {
        movieRepository.getGenres(object : OnDataLoadedCallback<GenresResponse> {

            override fun onError(e: Exception) {
                view?.onError(e)
            }

            override fun onSuccess(data: GenresResponse?) {
                data ?: return
                view?.onGetGenresSuccess(data.listGenres)
            }
        })
    }

    override fun getMovie(type: String, query: String, page: Int) {
        movieRepository.getMovies(
            type,
            query,
            page,
            object : OnDataLoadedCallback<MoviesResponse> {

                override fun onError(e: Exception) {
                    view?.onError(e)
                }

                override fun onSuccess(data: MoviesResponse?) {
                    data ?: return
                    when (type) {
                        UrlConstant.BASE_NOW_PLAYING ->
                            view?.onGetMoviesNowPlayingSuccess(data.listMovie)
//                        UrlConstant.BASE_UPCOMING ->
//                            view?.onGetMoviesUpcomingSuccess(data.listMovie)
                        UrlConstant.BASE_POPULAR ->
                            view?.onGetMoviesPopularSuccess(data.listMovie)
                        UrlConstant.BASE_GENRES_ID ->
                            view?.onGetMoviesByGenresIDSuccess(data.listMovie)
                    }
                    view?.onLoading(true)
                }
            })
    }
}
