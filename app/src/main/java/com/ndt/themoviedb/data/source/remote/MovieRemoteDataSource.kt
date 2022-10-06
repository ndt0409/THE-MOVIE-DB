package com.ndt.themoviedb.data.source.remote

import com.ndt.themoviedb.data.source.MovieDataSource
import com.ndt.themoviedb.data.source.remote.fetchjson.GenresResponseHandler
import com.ndt.themoviedb.data.source.remote.fetchjson.GetDataFromUrl
import com.ndt.themoviedb.data.source.remote.fetchjson.MovieDetailsResponseHandler
import com.ndt.themoviedb.data.source.remote.fetchjson.MoviesResponseHandler
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.utils.constant.APIConstant
import com.ndt.themoviedb.ui.utils.constant.UrlConstant

class MovieRemoteDataSource : MovieDataSource.Remote {

    override fun getGenres(listener: OnDataLoadedCallback<GenresResponse>) {
        val url =
            UrlConstant.BASE_URL +
                    UrlConstant.BASE_GENRES_LIST +
                    UrlConstant.BASE_API_KEY +
                    UrlConstant.BASE_LANGUAGE
        GetDataFromUrl(GenresResponseHandler(), listener).execute(url)
    }

    override fun getMovies(
        type: String,
        query: String,
        page: Int,
        listener: OnDataLoadedCallback<MoviesResponse>
    ) {

        val url = UrlConstant.BASE_URL +
                when (type) {
                    UrlConstant.BASE_GENRES_ID -> UrlConstant.BASE_DISCOVER_MOVIE
                    else -> type
                } +
                UrlConstant.BASE_API_KEY +
                UrlConstant.BASE_LANGUAGE +
                UrlConstant.BASE_PAGE +
                page +
                when (type) {
                    UrlConstant.BASE_GENRES_ID -> type + query
                    else -> ""
                }
        GetDataFromUrl(MoviesResponseHandler(), listener).execute(url)
    }

    override fun getMovieDetails(
        movieID: Int,
        listener: OnDataLoadedCallback<MovieDetailsResponse>
    ) {
        val url = APIConstant.URL_MOVIE +
                movieID + APIConstant.QUERY_DETAIL
        GetDataFromUrl(MovieDetailsResponseHandler(), listener).execute(url)
    }

    companion object {
        private var instance: MovieRemoteDataSource? = null
        fun getInstance() =
            instance ?: MovieRemoteDataSource().also { instance = it }
    }
}
