package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class SearchMoviesPagingSource(
    private val repo: IMovie,
    private val query: String
) : BaseMoviesPagingSource() {
    override suspend fun getMovies(page: Int): List<MovieItem> {
        val movieList = repo.search(query = query, page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}