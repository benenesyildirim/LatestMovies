package com.tmdb.latestmovies.presentation.detail_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tmdb.latestmovies.common.Resource
import com.tmdb.latestmovies.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        observeMovie()

        return binding.root
    }

    private fun observeMovie() {
        with(viewModel) {
            getMovieLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Toast.makeText(context, result.data!!.original_title, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}