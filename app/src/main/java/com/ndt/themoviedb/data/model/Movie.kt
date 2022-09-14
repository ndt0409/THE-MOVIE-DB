package com.ndt.themoviedb.data.model

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val backdropPath: String = "",
    val voteCount: Int = 0,
    val voteAverage: Double = 0.0,
    val popularity: Double = 0.0,
    val releaseDate: String = "",
    val genreID: Int = 0
)
