package com.ndt.themoviedb.ui.search

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.utils.constant.UrlConstant

class SearchPresenter(private val movieRepository: MovieRepository) : SearchContract.Presenter {
    private var view: SearchContract.View? = null

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

    override fun getCategories() {
        movieRepository.getCategories(object : OnDataLoadedCallback<List<Category>> {
            override fun onSuccess(data: List<Category>?) {
                data ?: return
                view?.getCategoriesSuccess(data)
            }

            override fun onError(e: Exception) {
                view?.onError(e)
            }
        })
    }

    override fun getMovies(type: String, query: String, page: Int) {
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
                    view?.onGetMoviesTopRatedSuccess(data.listMovie)
                    view?.onLoading(true)
                }
            })
    }

    override fun onStart() {
        view?.onLoading(false)
        getGenres()
        getCategories()
        getMovies(UrlConstant.BASE_TOP_RATE)
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun setView(view: SearchContract.View?) {
        this.view = view
    }
}
