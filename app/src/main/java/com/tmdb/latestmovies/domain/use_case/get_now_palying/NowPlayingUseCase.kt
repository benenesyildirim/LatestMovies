package com.tmdb.latestmovies.domain.use_case.get_now_palying

import com.tmdb.latestmovies.domain.repository.MovieRepository
import javax.inject.Inject

class NowPlayingUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun getNowPlaying(key:String) = repository.getNowPlaying(key)
}