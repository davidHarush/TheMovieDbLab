package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovieService

class DiscoverPagingSource(
    private val repo: IMovieService,
    private val genreList: List<Int>,
    private val rating: Float
) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.discoverMovies(withGenres = genreList, rating = rating, page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}