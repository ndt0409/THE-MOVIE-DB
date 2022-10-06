package com.ndt.themoviedb.data.source.remote.fetchjson

import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import org.json.JSONObject

class MovieDetailsResponseHandler : ParseDataWithJson<MovieDetailsResponse> {
    override fun parseToObject(jsonData: String): MovieDetailsResponse =
        MovieDetailsResponse(JSONObject(jsonData))
}
