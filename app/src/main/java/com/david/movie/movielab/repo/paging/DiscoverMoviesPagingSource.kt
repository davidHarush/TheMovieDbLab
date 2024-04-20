package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class DiscoverMoviesPagingSource(
    private val repo: IMovie,
    private val genreList: List<Int>,
    private val rating: Float
) : BaseMoviesPagingSource() {
    override suspend fun getMovies(page: Int): List<MovieItem> {
        val movieList = repo.discoverMovies(withGenres = genreList, rating = rating, page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}