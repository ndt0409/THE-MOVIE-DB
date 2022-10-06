package com.ndt.themoviedb.data.source.remote.response

import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.data.model.MovieTrailer
import com.ndt.themoviedb.data.source.remote.fetchjson.ParseDataToObject
import org.json.JSONObject

data class MovieDetailsResponse(
    var movies: Movie,
    var genres: List<Genres>,
    var produce: List<Produce>,
    var casts: List<Cast>,
    var trailers: List<MovieTrailer>
) {
    constructor(jsonObject: JSONObject) : this(
        movies = Movie(jsonObject),
        genres = ParseDataToObject.parJsonToGenres(jsonObject),
        produce = ParseDataToObject.parJsonToProduce(jsonObject),
        casts = ParseDataToObject.parJsonToCasts(jsonObject),
        trailers = ParseDataToObject.parJsonToTrailer(jsonObject)
    )
}
