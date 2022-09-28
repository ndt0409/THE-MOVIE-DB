package com.ndt.themoviedb.data.source.remote.fetchjson

import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import org.json.JSONObject

class GenresResponseHandler : ParseDataWithJson<GenresResponse> {
    override fun parseToObject(jsonData: String): GenresResponse =
        GenresResponse(JSONObject(jsonData))
}
