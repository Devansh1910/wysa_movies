package com.hostelicloud.wysa_assignment.model

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    var isLiked: Boolean = false // New field for "Liked" status
)
