package com.david.movie.movielab.repo.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingRepo @Inject constructor() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private fun <T : Any> getPager(pagingSourceFactory: () -> PagingSource<Int, T>): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchPersonsStream(query: String): Flow<PagingData<Actor>> {
        return getPager { SearchPersonsPagingSource(TMDBService.person, query) }
    }


    fun getPopularPersonsStream(): Flow<PagingData<Actor>> {
        return getPager { PopularPersonsPagingSource(TMDBService.person) }
    }

    fun getTopRatedMovieStream(): Flow<PagingData<MovieItem>> {
        return getPager { TopRatedPagingSource(TMDBService.movie) }
    }

    fun getPopularMovieStream(): Flow<PagingData<MovieItem>> {
        return getPager { PopularPagingSource(TMDBService.movie) }
    }

    fun getNowPlayingMovieStream(): Flow<PagingData<MovieItem>> {
        return getPager { NowPlayingPagingSource(TMDBService.movie) }
    }

    fun getSearchMoviesStream(query: String): Flow<PagingData<MovieItem>> {
        return getPager { SearchPagingSource(TMDBService.movie, query) }
    }

    fun getDiscoverMoviesStream(
        genreList: List<Int>,
        rating: Float
    ): Flow<PagingData<MovieItem>> {
        return getPager {
            DiscoverPagingSource(
                repo = TMDBService.movie,
                genreList = genreList,
                rating = rating
            )
        }
    }


}