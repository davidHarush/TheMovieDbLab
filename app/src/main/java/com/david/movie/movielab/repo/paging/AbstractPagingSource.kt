package com.david.movie.movielab.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class AbstractPagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun getData(page: Int): List<T>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: 1
        return try {
            val dataItems = getData(pageNumber)

            LoadResult.Page(
                data = dataItems,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (dataItems.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}