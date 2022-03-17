package com.tmdb.latestmovies.presentation.detail_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tmdb.latestmovies.common.Resource
import com.tmdb.latestmovies.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
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
                        binding.detailLoading.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.detailLoading.visibility = GONE

                        binding.movie = result.data!!
                        setImdbClickListener("https://www.imdb.com/title/${result.data.imdb_id}")
                    }
                    is Resource.Error -> {
                        binding.detailLoading.visibility = GONE
                    }
                }
            }
        }
    }

    private fun setImdbClickListener(url: String) {
        binding.imdbIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context!!.startActivity(
                Intent.createChooser(
                    intent,
                    "Choose browser"
                )
            )


        }
    }
}