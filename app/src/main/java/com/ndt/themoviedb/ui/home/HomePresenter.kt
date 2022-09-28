package com.ndt.themoviedb.ui.home

import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.utils.UrlConstant

class HomePresenter(private val movieRepository: MovieRepository) : HomeContract.Presenter {
    private var view: HomeContract.View? = null

    override fun onStart() {
        view?.onLoading(false)
        getGenres()
    }

    override fun onStop() {
        //TODO implement later
    }

    override fun setView(view: HomeFragment) {
        this.view = view
    }

    override fun getGenres() {
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
                        UrlConstant.BASE_GENRES_ID ->
                            view?.onGetMoviesByGenresIDSuccess(data.listMovie)
                    }
                    view?.onLoading(true)
                }
            })
    }
}
