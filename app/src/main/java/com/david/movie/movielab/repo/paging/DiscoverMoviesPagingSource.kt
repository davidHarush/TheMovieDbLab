package com.david.movie.movielab.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie
import com.david.movie.notwork.dto.MovieList


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
            val movies = convertToMovieItemList(movieList)


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

    private fun convertToMovieItemList(movieList: MovieList?): List<MovieItem> {

        return movieList?.results?.map { movie ->
            MovieItem(
                backdrop_path = movie.backdrop_path,
                id = movie.id,
                original_language = movie.original_language,
                original_title = movie.original_title,
                overview = movie.overview,
                poster_path = movie.poster_path,
                release_date = movie.release_date,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.vote_average
            )
        } ?: emptyList()
    }
}