package com.tmdb.latestmovies.presentation.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tmdb.latestmovies.R
import com.tmdb.latestmovies.common.Constants.API_KEY
import com.tmdb.latestmovies.common.Constants.MOVIE_ID
import com.tmdb.latestmovies.common.Resource
import com.tmdb.latestmovies.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var upComingAdapter: UpComingRvAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var lastPage = 1
    private var totalComplete = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        observeUpComings()
        observeNowPlayings()

        setUpComingList()
        setPullToRefresh()

        return binding.root
    }

    private fun observeUpComings() {
        lastPage = 1
        with(viewModel) {
            getUpComing(API_KEY,lastPage)
            upComingLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        if (lastPage == 1) {
                            upComingAdapter.setMovies(result.data!!.results)
                            binding.upComingsRv.scrollToPosition(0)
                        } else {
                            upComingAdapter.addMovies(result.data!!.results)
                        }

                        totalComplete++
                        if (totalComplete == 2) {
                            totalComplete = 0
                            binding.pullToRefreshLayout.isRefreshing = false
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeNowPlayings() {
        with(viewModel) {
            getNowPlaying(API_KEY)
            nowPlayingLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        binding.nowPlayingVp.adapter = NowPlayingVpAdapter(result.data!!.results)
                        binding.viewpagerDots.setupWithViewPager(binding.nowPlayingVp)

                        totalComplete++
                        if (totalComplete == 2) {
                            totalComplete = 0
                            binding.pullToRefreshLayout.isRefreshing = false
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpComingList() {
        upComingAdapter = UpComingRvAdapter {
            Navigation.findNavController(binding.root)
                .navigate(
                    R.id.action_homeFragment_to_detailFragment,
                    bundleOf(MOVIE_ID to it)
                )
        }

        binding.upComingsRv.apply{
            adapter = upComingAdapter
            smoothScrollToPosition(adapter!!.itemCount)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(dy <= 0){
                        lastPage++
                        viewModel.getUpComing(API_KEY, lastPage)
                    }
                }
            })
        }
    }

    private fun setPullToRefresh() {
        binding.pullToRefreshLayout.apply {
            setOnRefreshListener {
                lastPage = 1
                with(viewModel) {
                    getNowPlaying(API_KEY)
                    getUpComing(API_KEY, lastPage)
                }
            }
            isNestedScrollingEnabled = true
        }
    }
}