package com.ndt.themoviedb.data.repository

import com.ndt.themoviedb.data.source.MovieDataSource
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse

class MovieRepository(
    private val remoteMovie: MovieDataSource.Remote
) {
    fun getGenres(listener: OnDataLoadedCallback<GenresResponse>) {
        remoteMovie.getGenres(listener)
    }

    fun getMovies(
        type: String,
        query: String,
        page: Int,
        listener: OnDataLoadedCallback<MoviesResponse>
    ) {
        remoteMovie.getMovies(type, query, page, listener)
    }

    fun getMovieDetails(
        movieID: Int,
        listener: OnDataLoadedCallback<MovieDetailsResponse>?
    ) {
        listener?.let { remoteMovie.getMovieDetails(movieID, it) }
    }

    companion object {
        private var instance: MovieRepository? = null
        fun getInstance(
            movieRemoteDataSource: MovieRemoteDataSource,
            instance: MovieLocalDataSource
        ) = this.instance ?: MovieRepository(
            movieRemoteDataSource
        ).also { this.instance = it }
    }
}
