package com.hostelicloud.wysa_assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hostelicloud.wysa_assignment.R
import com.hostelicloud.wysa_assignment.model.Movie
import com.hostelicloud.wysa_assignment.network.MoviePagingSource
import kotlinx.coroutines.flow.Flow

class MovieViewModel : ViewModel() {

    // Carousel images LiveData
    private val _carouselImages = MutableLiveData<List<Int>>()
    val carouselImages: LiveData<List<Int>> get() = _carouselImages

    // Loading status LiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Error message LiveData
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Liked movies LiveData
    private val _likedMovies = MutableLiveData<MutableList<Movie>>(mutableListOf())
    val likedMovies: LiveData<MutableList<Movie>> get() = _likedMovies

    init {
        // Initialize carousel images with placeholders
        _carouselImages.value = listOf(
            R.drawable.image1,
            R.drawable.image2
        )
    }

    // Function to get paginated movies with lazy loading
    fun getMovies(language: String? = null): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6,            // Load six items at a time
                prefetchDistance = 3,     // Start fetching the next page when 3 items away
                enablePlaceholders = false // Disable placeholders for better performance
            ),
            pagingSourceFactory = { MoviePagingSource(language) }
        ).flow.cachedIn(viewModelScope)
    }

    // Handle error state by clearing error LiveData
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
