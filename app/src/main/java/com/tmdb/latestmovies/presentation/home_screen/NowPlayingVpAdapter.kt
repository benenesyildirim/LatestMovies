package com.tmdb.latestmovies.presentation.home_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tmdb.latestmovies.R
import com.tmdb.latestmovies.data.remote.dto.MovieDto
import com.tmdb.latestmovies.databinding.NowPlayingPagerDesignBinding

class NowPlayingVpAdapter(private val movieList: List<MovieDto>, val listener: (Int) -> Unit) : PagerAdapter() {
    private var lastPosition: Int = 0

    override fun getCount() = movieList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = LayoutInflater.from(container.context)
        val binding = NowPlayingPagerDesignBinding.inflate(inflater)
        val view = binding.root
        binding.movie = movieList[position]
        binding.root.setOnClickListener { listener(movieList[position].id.toInt()) }

        val vp = container as ViewPager
        vp.addView(view, 0)

        val animation: Animation = AnimationUtils.loadAnimation(view.context, if (position > lastPosition) R.anim.left_from_right else R.anim.right_from_left)
        animation.duration = 1
        view.startAnimation(animation)
        lastPosition = position

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}
