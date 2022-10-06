package com.ndt.themoviedb.data.source.remote.fetchjson


import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.MovieResultPage
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.data.model.MovieTrailer
import org.json.JSONObject

object ParseDataToObject {
    fun parJsonToMovieResultPage(jsonObject: JSONObject): MovieResultPage {
        return MovieResultPage(jsonObject)
    }

    fun parJsonToMovies(jsonObject: JSONObject): MutableList<Movie> {
        val listMovies = mutableListOf<Movie>()
        val jsonArray = jsonObject.getJSONArray(Movie.MovieEntry.MOVIE)
        for (i in 0 until jsonArray.length()) {
            listMovies.add(Movie(jsonArray.getJSONObject(i)))
        }
        return listMovies
    }

    fun parJsonToGenres(jsonObject: JSONObject): MutableList<Genres> {
        val listGenres = mutableListOf<Genres>()
        val jsonArray = jsonObject.getJSONArray(Genres.GenresEntry.GENRES)
        for (i in 0 until jsonArray.length()) {
            listGenres.add(Genres(jsonArray.getJSONObject(i)))
        }
        return listGenres
    }

    fun parJsonToCasts(jsonObject: JSONObject): MutableList<Cast> {
        val listCast = mutableListOf<Cast>()
        val jsonObjectCredits = jsonObject.getJSONObject(Cast.CastEntry.CREDITS)
        val jsonArray = jsonObjectCredits.getJSONArray(Cast.CastEntry.CAST)
        for (i in 0 until jsonArray.length()) {
            listCast.add(Cast(jsonArray.getJSONObject(i)))
        }
        return listCast
    }

    fun parJsonToProduce(jsonObject: JSONObject): MutableList<Produce> {
        val listProduce = mutableListOf<Produce>()
        val jsonArray = jsonObject.getJSONArray(Produce.ProduceEntry.PRODUCES)
        for (i in 0 until jsonArray.length()) {
            listProduce.add(Produce(jsonArray.getJSONObject(i)))
        }
        return listProduce
    }

    fun parJsonToTrailer(jsonObject: JSONObject): MutableList<MovieTrailer> {
        val listTrailer = mutableListOf<MovieTrailer>()
        val jsonObjectVideo =
            jsonObject.getJSONObject(MovieTrailer.MovieTrailerEntry.TRAILER_VIDEO)
        val jsonArray =
            jsonObjectVideo.getJSONArray(MovieTrailer.MovieTrailerEntry.TRAILER_RESULTS)
        for (i in 0 until jsonArray.length()) {
            listTrailer.add(MovieTrailer(jsonArray.getJSONObject(i)))
        }
        return listTrailer
    }
}
