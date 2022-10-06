package com.ndt.themoviedb.data.source.local

class MovieLocalDataSource private constructor() {

    companion object {
        private var instance: MovieLocalDataSource? = null
        fun getInstance() =
            instance ?: MovieLocalDataSource().also { instance = it }
    }
}
