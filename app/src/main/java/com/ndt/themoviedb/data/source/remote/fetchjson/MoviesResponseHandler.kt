package com.ndt.themoviedb.data.source.remote.fetchjson

import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import org.json.JSONObject

class MoviesResponseHandler : ParseDataWithJson<MoviesResponse> {
    override fun parseToObject(jsonData: String): MoviesResponse =
        MoviesResponse(JSONObject(jsonData))
}
