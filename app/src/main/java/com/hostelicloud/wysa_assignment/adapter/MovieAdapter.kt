package com.hostelicloud.wysa_assignment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.model.Movie

class MovieAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit // Change parameter type to Movie
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    class MovieViewHolder(
        itemView: View,
        private val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleView: TextView = itemView.findViewById(R.id.movieTitle)

        fun bind(movie: Movie) {
            titleView.text = movie.title
            Glide.with(imageView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(imageView)

            // Set up click listener to invoke the lambda function
            itemView.setOnClickListener {
                onMovieClick(movie) // Pass the movie object directly
            }
        }
    }
}
