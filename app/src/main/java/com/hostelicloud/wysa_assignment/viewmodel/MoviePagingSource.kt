package com.hostelicloud.wysa_assignment.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hostelicloud.wysa_assignment.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(private val language: String?) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1 // Start loading from page 1 if undefined
        return try {
            val response = RetrofitInstance.api.getMovies(
                apiKey = "9f55240a79957004a278b1603aa6d403",
                language = language ?: "all",
                page = page
            )
            val movies = response.body()?.results ?: emptyList()

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1, // Previous page, if any
                nextKey = if (movies.isEmpty()) null else page + 1 // Next page if data available
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
