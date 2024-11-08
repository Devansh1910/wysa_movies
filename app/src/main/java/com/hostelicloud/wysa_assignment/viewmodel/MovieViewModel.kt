package com.hostelicloud.wysa_assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.model.Movie
import com.hostelicloud.wysa_assignment.network.RetrofitInstance
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _carouselImages = MutableLiveData<List<Int>>()
    val carouselImages: LiveData<List<Int>> get() = _carouselImages

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // New MutableLiveData to store liked movies
    private val _likedMovies = MutableLiveData<MutableList<Movie>>(mutableListOf())
    val likedMovies: LiveData<MutableList<Movie>> get() = _likedMovies

    init {
        // Initialize carousel images
        _carouselImages.value = listOf(
            R.drawable.image1,
            R.drawable.image2
        )

        // Fetch movies on initialization
        fetchMovies("9f55240a79957004a278b1603aa6d403")
    }

    fun fetchMovies(language: String?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovies(
                    apiKey = "9f55240a79957004a278b1603aa6d403",
                    language = language ?: "all"
                )
                if (response.isSuccessful) {
                    _movies.value = response.body()?.results
                    _error.value = null
                } else {
                    _error.value = "Failed to load movies"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun errorHandled() {
        _error.value = null
    }

    // Method to add or remove a movie from liked movies
    fun toggleLikedMovie(movie: Movie) {
        _likedMovies.value?.let { likedMoviesList ->
            if (movie.isLiked) {
                // Add movie to liked list if it's not already there
                if (!likedMoviesList.contains(movie)) {
                    likedMoviesList.add(movie)
                }
            } else {
                // Remove movie from liked list if it's already there
                likedMoviesList.remove(movie)
            }
            // Update the LiveData to notify observers
            _likedMovies.value = likedMoviesList
        }
    }
}
