package com.hostelicloud.wysa_assignment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.model.Movie

class MoviePagingAdapter(
    private val onMovieClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    class MovieViewHolder(
        itemView: View,
        private val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleView: TextView = itemView.findViewById(R.id.movieTitle)

        fun bind(movie: Movie) {
            // Set movie title
            titleView.text = movie.title

            // Step 1: Display placeholder first for smoother UI
            Glide.with(imageView.context)
                .load(R.drawable.image1) // Load placeholder image immediately
                .into(imageView)

            // Step 2: Gradually load actual image with caching for smooth scrolling
            Glide.with(imageView.context)
                .load("https://image.tmdb.org/t/p/w200${movie.poster_path}")
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Cache for future use
                        .placeholder(R.drawable.image1) // Show placeholder while loading
                        .error(R.drawable.image2) // Show error if image fails to load
                )
                .thumbnail(0.1f) // Gradually load the full image after a low-quality thumbnail
                .into(imageView)

            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}
