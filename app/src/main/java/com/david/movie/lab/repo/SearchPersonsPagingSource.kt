package com.david.movie.lab.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.isValid
import com.david.movie.notwork.IPerson

class SearchPersonsPagingSource(
    private val repo: IPerson,
    private val query: String
) : PagingSource<Int, Actor>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actor> {
        val pageNumber = params.key ?: 1
        return try {
            val response = repo.search(query = query, page = pageNumber)
            Log.d("SearchPersonsPagingSource", "load: ${response?.results}")
            val persons = response?.results?.map { person ->
                Actor(person)
            }
            LoadResult.Page(
                data = persons?.filter { person -> person.isValid() }!!,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (persons.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Actor>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
