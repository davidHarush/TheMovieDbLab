package com.david.movie.movielab.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.notwork.IPerson


class PopularMoviesPagingSource(
    private val repo: IPerson,
) : PagingSource<Int, Actor>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actor> {
        val pageNumber = params.key ?: 1
        return try {
            val response = repo.getPopular(page = pageNumber)
            val persons = response?.results?.map { person ->
                Actor(person)
            }
            LoadResult.Page(
                data = persons ?: emptyList(),
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (persons?.isEmpty() == true) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Actor>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}