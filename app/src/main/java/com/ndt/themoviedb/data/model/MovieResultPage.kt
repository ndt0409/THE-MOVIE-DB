package com.ndt.themoviedb.data.model

import org.json.JSONObject

data class MovieResultPage(
    val page: Int,
    val totalResult: Int,
    val totalPage: Int
) {
    constructor(json: JSONObject) : this(
        page = json.getInt(MovieMovieResultPageEntry.PAGE),
        totalResult = json.getInt(MovieMovieResultPageEntry.TOTAL_RESULT),
        totalPage = json.getInt(MovieMovieResultPageEntry.TOTAL_PAGES)
    )

    object MovieMovieResultPageEntry {
        const val PAGE = "page"
        const val TOTAL_RESULT = "total_results"
        const val TOTAL_PAGES = "total_pages"
    }
}
