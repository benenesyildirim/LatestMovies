package com.tmdb.latestmovies.data.remote

import com.tmdb.latestmovies.data.remote.dto.MovieDetailDto
import com.tmdb.latestmovies.data.remote.dto.MoviesResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") key: String): Response<MoviesResultDto>

    @GET("3/movie/upcoming")
    suspend fun getUpComing(@Query("api_key") key: String, @Query("page") page: Int): Response<MoviesResultDto>

    @GET("3/movie/{ID}")
    suspend fun getMovieDetail( @Path("ID") id: Int, @Query("api_key") key: String): Response<MovieDetailDto>
}