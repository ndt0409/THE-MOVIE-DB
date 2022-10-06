package com.ndt.themoviedb.data.model

import org.json.JSONObject

data class MovieTrailer(
    val trailerID: String,
    val trailerKey: String,
    val trailerName: String
) {
    constructor(jsonObject: JSONObject) : this(
        trailerID = jsonObject.optString(MovieTrailerEntry.TRAILER_ID),
        trailerKey = jsonObject.optString(MovieTrailerEntry.TRAILER_KEY),
        trailerName = jsonObject.optString(MovieTrailerEntry.TRAILER_NAME)
    )

    object MovieTrailerEntry {
        const val TRAILER_VIDEO = "videos"
        const val TRAILER_RESULTS = "results"
        const val TRAILER_ID = "id"
        const val TRAILER_KEY = "key"
        const val TRAILER_NAME = "name"
    }
}
