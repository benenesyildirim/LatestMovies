package com.tmdb.latestmovies.data.remote.dto

data class MovieDetailDto(
    val imdb_id: String,
    val original_title: String,
    val overview: String,
    val backdrop_path: String,
    val vote_average: Double,
    val release_date: String
)