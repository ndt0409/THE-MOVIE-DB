package com.ndt.themoviedb.data.source.remote.response

import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.MovieResultPage
import com.ndt.themoviedb.data.source.remote.fetchjson.ParseDataToObject
import org.json.JSONObject

data class MoviesResponse(val movieResultPage: MovieResultPage, val listMovie: MutableList<Movie>) {
    constructor(jsonObject: JSONObject) : this(
        movieResultPage = ParseDataToObject.parJsonToMovieResultPage(jsonObject),
        listMovie = ParseDataToObject.parJsonToMovies(jsonObject)
    )
}
