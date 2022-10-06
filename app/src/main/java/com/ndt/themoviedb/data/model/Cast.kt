package com.ndt.themoviedb.data.model

import org.json.JSONObject

data class Cast(
    val id: Int,
    val name: String,
    val profilePath: String
) {
    constructor(castJson: JSONObject) : this(
        id = castJson.optInt(CastEntry.ID),
        name = castJson.optString(CastEntry.NAME),
        profilePath = castJson.optString(CastEntry.PROFILE_PATH)
    )

    object CastEntry {
        const val CREDITS = "credits"
        const val CAST = "cast"
        const val ID = "id"
        const val NAME = "name"
        const val PROFILE_PATH = "profile_path"
    }
}
