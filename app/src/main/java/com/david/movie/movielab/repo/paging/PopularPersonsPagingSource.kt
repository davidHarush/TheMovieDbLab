package com.david.movie.movielab.repo.paging


import com.david.movie.movielab.repo.model.Actor
import com.david.movie.notwork.IPersonService

class PopularPersonsPagingSource(private val repo: IPersonService) : AbstractPagingSource<Actor>() {
    override suspend fun getData(page: Int): List<Actor> {
        val data = repo.getPopular(page = page)
        val persons = data?.results?.map { person ->
            Actor(person)
        }
        return persons ?: emptyList()
    }
}