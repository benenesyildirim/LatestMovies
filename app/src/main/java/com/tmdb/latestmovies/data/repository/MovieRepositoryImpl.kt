package com.tmdb.latestmovies.data.repository

import com.tmdb.latestmovies.data.remote.MovieApi
import com.tmdb.latestmovies.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: MovieApi) : MovieRepository {
    override suspend fun getNowPlaying(key: String) = api.getNowPlaying(key)

    override suspend fun getUpComing(key: String, page: Int) = api.getUpComing(key, page)

    override suspend fun getMovieDetail(key: String, id: Int) = api.getMovieDetail(id, key)
}