package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IMovie

class NowPlayingPagingSource(private val repo: IMovie) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.getNowPlaying(page = page)
        return mapTMDBMovieListToMovieItemList(movieList)
    }
}