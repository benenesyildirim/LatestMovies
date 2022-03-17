package com.tmdb.latestmovies.presentation.detail_screen

import androidx.lifecycle.*
import com.tmdb.latestmovies.common.Constants.API_KEY
import com.tmdb.latestmovies.common.Constants.MOVIE_ID
import com.tmdb.latestmovies.common.Resource
import com.tmdb.latestmovies.data.remote.dto.MovieDetailDto
import com.tmdb.latestmovies.domain.use_case.get_movie.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        getMovieDetail(API_KEY,savedStateHandle.get<Int>(MOVIE_ID)!!)
    }

    private val _getMovieLiveData = MutableLiveData<Resource<MovieDetailDto>>()
    val getMovieLiveData: LiveData<Resource<MovieDetailDto>> get() = _getMovieLiveData

    private fun getMovieDetail(key: String, id: Int) = viewModelScope.launch {
        //_getMovieLiveData.postValue(Resource.Loading())

        try {
            getMovieUseCase.getMovieDetail(key,id).let {
                if (it.isSuccessful)
                    _getMovieLiveData.postValue(Resource.Success(it.body()!!))
            }
        } catch (e: HttpException) {
            _getMovieLiveData.postValue(
                Resource.Error(
                    e.localizedMessage
                        ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _getMovieLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}