package com.ndt.themoviedb.data.source

import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse

interface MovieDataSource {
    interface Remote {

        fun getGenres(
            listener: OnDataLoadedCallback<GenresResponse>
        )

        fun getMovies(
            type: String,
            query: String,
            page: Int,
            listener: OnDataLoadedCallback<MoviesResponse>
        )
    }
}
