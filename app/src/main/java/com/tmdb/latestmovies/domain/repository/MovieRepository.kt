package com.tmdb.latestmovies.domain.repository

import com.tmdb.latestmovies.data.remote.dto.MovieDetailDto
import com.tmdb.latestmovies.data.remote.dto.MoviesResultDto
import retrofit2.Response

interface MovieRepository {
    suspend fun getNowPlaying(key: String): Response<MoviesResultDto>

    suspend fun getUpComing(key: String, page: Int): Response<MoviesResultDto>

    suspend fun getMovieDetail(key: String, id: Int): Response<MovieDetailDto>
}