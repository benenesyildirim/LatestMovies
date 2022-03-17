package com.tmdb.latestmovies.presentation.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
        with(viewModel) {
            upComingLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.homeLoading.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        if (lastPage == 1){
                            upComingAdapter.clearMovies()
                        }
                        upComingAdapter.addMovies(result.data!!.results)

                        totalComplete++
                        if (totalComplete == 2) {
                            totalComplete = 0
                            binding.pullToRefreshLayout.isRefreshing = false
                            binding.homeLoading.visibility = GONE
                        }
                    }
                    is Resource.Error -> {
                        binding.homeLoading.visibility = GONE

                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeNowPlayings() {
        with(viewModel) {
            nowPlayingLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.homeLoading.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.nowPlayingVp.adapter = NowPlayingVpAdapter(result.data!!.results) {
                            Navigation.findNavController(binding.root)
                                .navigate(
                                    R.id.action_homeFragment_to_detailFragment,
                                    bundleOf(MOVIE_ID to it)
                                )
                        }
                        binding.viewpagerDots.setupWithViewPager(binding.nowPlayingVp)

                        totalComplete++
                        if (totalComplete == 2) {
                            totalComplete = 0
                            binding.pullToRefreshLayout.isRefreshing = false
                            binding.homeLoading.visibility = GONE
                        }
                    }
                    is Resource.Error -> {
                        binding.homeLoading.visibility = GONE

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

        binding.upComingsRv.apply {
            adapter = upComingAdapter
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.layoutManager = layoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val lastPosition = linearLayoutManager!!.findLastCompletelyVisibleItemPosition()
                    if (lastPosition == upComingAdapter.itemCount - 1) {
                        totalComplete = 1
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