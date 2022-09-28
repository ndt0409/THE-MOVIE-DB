package com.ndt.themoviedb.data.model

import org.json.JSONObject

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val overView: String = "",
    val posterPath: String = "",
    val backdropPath: String = "",
    val voteCount: Int = 0,
    val voteAverage: Double = 0.0,
    val releaseDate: String = ""
) {
    constructor(movieJson: JSONObject) : this(
        id = movieJson.optInt(MovieEntry.ID),
        title = movieJson.optString(MovieEntry.TITLE),
        overView = movieJson.optString(MovieEntry.OVERVIEW),
        posterPath = movieJson.optString(MovieEntry.POSTER_PATH),
        backdropPath = movieJson.optString(MovieEntry.BACKDROP_PATH),
        voteAverage = movieJson.optDouble(MovieEntry.VOTE_AVERAGE),
        releaseDate = movieJson.optString(MovieEntry.RELEASE_DATE)
    )

    object MovieEntry {
        const val MOVIE = "results"
        const val ID = "id"
        const val TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTER_PATH = "poster_path"
        const val BACKDROP_PATH = "backdrop_path"
        const val VOTE_AVERAGE = "vote_average"
        const val RELEASE_DATE = "release_date"
    }
}
