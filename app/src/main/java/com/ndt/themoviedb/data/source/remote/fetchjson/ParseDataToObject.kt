package com.ndt.themoviedb.data.source.remote.fetchjson

import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.MovieResultPage
import org.json.JSONObject

object ParseDataToObject {
    fun parJsonToMovieResultPage(jsonObject: JSONObject): MovieResultPage {
        return MovieResultPage(jsonObject)
    }

    fun parJsonToMovies(jsonObject: JSONObject): MutableList<Movie> {
        val movies = mutableListOf<Movie>()
        val jsonArray = jsonObject.getJSONArray(Movie.MovieEntry.MOVIE)
        for (i in 0 until jsonArray.length()) {
            movies.add(Movie(jsonArray.getJSONObject(i)))
        }
        return movies
    }

    fun parJsonToGenres(jsonObject: JSONObject): MutableList<Genres> {
        val listGenres = mutableListOf<Genres>()
        val jsonArray = jsonObject.getJSONArray(Genres.GenresEntry.GENRES)
        for (i in 0 until jsonArray.length()) {
            listGenres.add(Genres(jsonArray.getJSONObject(i)))
        }
        return listGenres
    }
}
