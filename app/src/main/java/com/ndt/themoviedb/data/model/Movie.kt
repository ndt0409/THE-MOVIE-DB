package com.ndt.themoviedb.data.model

data class Movie(
    var ID: Int?,
    var Title: String?,
    var Overview: String?,
    var PosterPath: String?,
    var BackdropPath: String?,
    var VoteCount: Int?,
    var VoteAverage: Double?,
    var Popularity: Double?,
    var ReleaseDate: String?,
    val GenreID: IntArray?
)
