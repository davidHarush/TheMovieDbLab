package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class NowPlayingMoviesPagingSource(private val repo: IMovie) : BaseMoviesPagingSource() {
    override suspend fun getMovies(page: Int): List<MovieItem> {
        val movieList = repo.getNowPlaying(page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}