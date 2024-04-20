package com.david.movie.movielab.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie


class DiscoverMoviesPagingSource(
    private val repo: IMovie,
    private val genreList: List<Int>,
    private val rating: Float
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val pageNumber = params.key ?: 1
        return try {
            val movieList =
                repo.discoverMovies(withGenres = genreList, rating = rating, page = pageNumber)
            val movies = mapTMDBMovieListToMovieItemList(movieList)


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
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}