package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class SearchPagingSource(
    private val repo: IMovie,
    private val query: String
) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.search(query = query, page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}