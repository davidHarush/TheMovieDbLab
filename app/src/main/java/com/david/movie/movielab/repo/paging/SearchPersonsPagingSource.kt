package com.david.movie.movielab.repo.paging

import com.david.movie.movielab.repo.model.Actor
import com.david.movie.notwork.IPersonService


class SearchPersonsPagingSource(private val repo: IPersonService, private val query: String) :
    AbstractPagingSource<Actor>() {
    override suspend fun getData(page: Int): List<Actor> {
        val data = repo.search(query = query, page = page)
        val persons = data?.results?.map { person ->
            Actor(person)
        }
        return persons ?: emptyList()
    }
}
