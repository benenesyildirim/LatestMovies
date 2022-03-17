package com.tmdb.latestmovies.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tmdb.latestmovies.R

class Utils {
    companion object {
        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)
            }
        }

        @BindingAdapter("loadMovieImage")
        @JvmStatic
        fun loadMovieImage(imageView: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load("https://image.tmdb.org/t/p/w500$imageUrl")
                    .placeholder(R.drawable.cinema_placeholder)
                    .into(imageView)
            }
        }
    }
}