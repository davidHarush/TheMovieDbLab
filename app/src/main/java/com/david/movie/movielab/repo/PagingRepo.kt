package com.david.movie.movielab.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingRepo @Inject constructor() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    fun searchPersonsStream(query: String): Flow<PagingData<Actor>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { SearchPersonsPagingSource(TMDBService.person, query) }
        ).flow
    }


    fun getPopularPersonsStream(): Flow<PagingData<Actor>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularMoviesPagingSource(TMDBService.person) }
        ).flow
    }

    fun getSearchMoviesStream(query: String): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                SearchMoviesPagingSource(repo = TMDBService.movie, query = query)
            }
        ).flow
    }

    fun getDiscoverMoviesStream(
        genreList: List<Int>,
        rating: Float
    ): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                DiscoverMoviesPagingSource(
                    repo = TMDBService.movie,
                    genreList = genreList,
                    rating = rating
                )
            }
        ).flow
    }

}