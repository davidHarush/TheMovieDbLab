package com.david.movie.movielab.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.movielab.repo.model.MovieItem


abstract class BaseMoviesPagingSource : PagingSource<Int, MovieItem>() {

    abstract suspend fun getMovies(page: Int): List<MovieItem>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val pageNumber = params.key ?: 1
        return try {
            val movies = getMovies(pageNumber)

            LoadResult.Page(
                data = movies,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (movies.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
