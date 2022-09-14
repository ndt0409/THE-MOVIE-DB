package com.ndt.themoviedb.ui.utils

import com.ndt.themoviedb.BuildConfig

object Constant {
    const val BASE_URL = "https://api.themoviedb.org/3"
    const val BASE_GENRES_LIST = "/genre/movie/list"
    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
    const val BASE_GENRES_ID = "&with_genres="
    const val BASE_MOVIE_BY_ID = "/discover/movie"
    const val BASE_NOW_PLAYING = "/movie/now_playing"
    const val BASE_POPULAR = "/movie/popular"
    const val BASE_TOP_RATE = "/movie/top_rated"
    const val BASE_UPCOMING = "/movie/upcoming"
    const val BASE_PAGE = "?page=1"
    const val BASE_API_KEY = "?api_key" + BuildConfig.API_KEY
}
