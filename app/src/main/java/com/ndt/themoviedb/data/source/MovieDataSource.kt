package com.ndt.themoviedb.data.source

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse

interface MovieDataSource {

    interface Local {
        fun getCategories(listener: OnDataLoadedCallback<List<Category>>)
    }

    interface Remote {

        fun getGenres(listener: OnDataLoadedCallback<GenresResponse>)

        fun getMovies(
            type: String,
            query: String,
            page: Int,
            listener: OnDataLoadedCallback<MoviesResponse>
        )

        fun getMovieDetails(
            movieID: Int, listener:
            OnDataLoadedCallback<MovieDetailsResponse>
        )
    }
}
