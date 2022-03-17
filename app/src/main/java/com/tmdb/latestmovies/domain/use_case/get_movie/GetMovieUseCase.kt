package com.tmdb.latestmovies.domain.use_case.get_movie

import com.tmdb.latestmovies.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun getMovieDetail(key: String, id: Int) = repository.getMovieDetail(key, id)
}