package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class PopularMoviesPagingSource(private val repo: IMovie) : BaseMoviesPagingSource() {
    override suspend fun getMovies(page: Int): List<MovieItem> {
        val movieList = repo.getPopular(page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}
