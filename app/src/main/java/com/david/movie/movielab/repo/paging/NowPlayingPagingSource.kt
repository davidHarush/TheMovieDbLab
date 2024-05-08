package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.model.isValid
import com.david.movie.notwork.IMovieService

class NowPlayingPagingSource(private val repo: IMovieService) : AbstractPagingSource<MovieItem>() {
    override suspend fun getData(page: Int): List<MovieItem> {
        val movieList = repo.getNowPlaying(page = page)
        return mapTMDBMovieListToMovieItemList(movieList).filter { it.isValid() }
    }
}