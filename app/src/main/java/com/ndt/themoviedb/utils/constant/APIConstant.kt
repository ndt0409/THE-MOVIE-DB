package com.ndt.themoviedb.utils.constant

import com.ndt.themoviedb.BuildConfig

object APIConstant {
    const val URL_MOVIE = "https://api.themoviedb.org/3/movie/"
    const val QUERY_DETAIL =
        "?api_key" + BuildConfig.API_KEY + "&language=vi-VN&append_to_response=credits,videos"
}
