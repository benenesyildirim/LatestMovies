package com.tmdb.latestmovies.data.remote.dto

data class MovieDto(
    val id: Long,
    val title: String,
    val poster_path: String,
    val overview: String,
    val release_date: String
)
