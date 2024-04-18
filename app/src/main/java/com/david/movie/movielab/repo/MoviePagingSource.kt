package com.david.movie.movielab.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.movielab.repo.model.MovieItem


class MoviePagingSource(
    private val movieRepo: MovieRepo
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = movieRepo.getPopularMovieList(pageNumber)
            LoadResult.Page(
                data = response,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (response.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
