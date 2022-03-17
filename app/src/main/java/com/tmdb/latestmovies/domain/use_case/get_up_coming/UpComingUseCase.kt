package com.tmdb.latestmovies.domain.use_case.get_up_coming

import com.tmdb.latestmovies.domain.repository.MovieRepository
import javax.inject.Inject

class UpComingUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun getUpComing(key: String, page: Int) = repository.getUpComing(key, page)
}