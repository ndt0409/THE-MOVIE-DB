package com.ndt.themoviedb.data.source.remote.response

import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.source.remote.fetchjson.ParseDataToObject
import org.json.JSONObject

data class GenresResponse(val listGenres: MutableList<Genres>) {
    constructor(jsonObject: JSONObject) : this(
        listGenres = ParseDataToObject.parJsonToGenres(jsonObject)
    )
}
