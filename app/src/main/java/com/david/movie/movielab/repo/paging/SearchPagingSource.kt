package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.model.isValid
import com.david.movie.notwork.IMovieService

class SearchPagingSource(
    private val repo: IMovieService,
    private val query: String
) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.search(query = query, page = page)
        return mapTMDBMovieListToMovieItemList(movieList).filter { it.isValid() }
    }
}