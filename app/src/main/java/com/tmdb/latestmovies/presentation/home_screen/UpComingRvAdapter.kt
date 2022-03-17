package com.tmdb.latestmovies.presentation.home_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tmdb.latestmovies.data.remote.dto.MovieDto
import com.tmdb.latestmovies.databinding.UpComingRowDesignBinding
import okhttp3.internal.notifyAll

class UpComingRvAdapter(val listener: (Int) -> Unit) :
    RecyclerView.Adapter<UpComingRvAdapter.ViewHolder>() {
    private val movieList: MutableList<MovieDto> = mutableListOf()

    inner class ViewHolder(private val binding: UpComingRowDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDto) {
            binding.apply {
                this.movie = movie
                binding.root.setOnClickListener { listener(movie.id.toInt()) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UpComingRowDesignBinding.inflate(inflater)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(movieList[position])

    override fun getItemCount() = movieList.size

    fun addMovies(movies: List<MovieDto>) {
        movieList.addAll(movies)
        notifyDataSetChanged()
    }

    fun clearMovies(){
        movieList.clear()
    }
}