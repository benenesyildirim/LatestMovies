package com.tmdb.latestmovies.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.latestmovies.common.Constants.API_KEY
import com.tmdb.latestmovies.common.Resource
import com.tmdb.latestmovies.data.remote.dto.MoviesResultDto
import com.tmdb.latestmovies.domain.use_case.get_now_palying.NowPlayingUseCase
import com.tmdb.latestmovies.domain.use_case.get_up_coming.UpComingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nowPlayingUseCase: NowPlayingUseCase,
    private val upComingUseCase: UpComingUseCase
) : ViewModel() {

    init{
        getNowPlaying(API_KEY)
        getUpComing(API_KEY,1)
    }

    private val _nowPlayingLiveData = MutableLiveData<Resource<MoviesResultDto>>()
    val nowPlayingLiveData: LiveData<Resource<MoviesResultDto>> get() = _nowPlayingLiveData

    private val _upComingLiveData = MutableLiveData<Resource<MoviesResultDto>>()
    val upComingLiveData: LiveData<Resource<MoviesResultDto>> get() = _upComingLiveData

    private fun getNowPlaying(key: String) = viewModelScope.launch {
        //_nowPlayingLiveData.postValue(Resource.Loading())

        try {
            nowPlayingUseCase.getNowPlaying(key).let {
                if (it.isSuccessful)
                    _nowPlayingLiveData.postValue(Resource.Success(it.body()!!))
            }
        } catch (e: HttpException) {
            _nowPlayingLiveData.postValue(
                Resource.Error(
                    e.localizedMessage
                        ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _nowPlayingLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private fun getUpComing(key: String, page: Int) = viewModelScope.launch {
        //_upComingLiveData.postValue(Resource.Loading())

        try {
            upComingUseCase.getUpComing(key, page).let {
                if (it.isSuccessful)
                    _upComingLiveData.postValue(Resource.Success(it.body()!!))
            }
        } catch (e: HttpException) {
            _upComingLiveData.postValue(
                Resource.Error(
                    e.localizedMessage
                        ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _upComingLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}