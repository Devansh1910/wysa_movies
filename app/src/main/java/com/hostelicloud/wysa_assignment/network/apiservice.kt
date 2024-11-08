package com.hostelicloud.wysa_assignment.network

import com.hostelicloud.wysa_assignment.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "all",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>
}
