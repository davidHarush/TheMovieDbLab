package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.model.isValid
import com.david.movie.notwork.IMovieService

class TopRatedPagingSource(private val repo: IMovieService) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.getTopRated(page = page)
        return mapTMDBMovieListToMovieItemList(movieList).filter { it.isValid() }
    }
}