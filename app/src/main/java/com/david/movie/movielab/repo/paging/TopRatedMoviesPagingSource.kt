package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class TopRatedMoviesPagingSource(private val repo: IMovie) : BaseMoviesPagingSource() {
    override suspend fun getMovies(page: Int): List<MovieItem> {
        val movieList = repo.getTopRated(page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}