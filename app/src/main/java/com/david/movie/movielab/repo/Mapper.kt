package com.david.movie.movielab.repo

import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.MovieDetailsItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.dto.CastMemberTMDB
import com.david.movie.notwork.dto.MovieCastCreditTMDB
import com.david.movie.notwork.dto.MovieDetailsTMDB
import com.david.movie.notwork.dto.MovieItemTMDB
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.SimilarMovieTMDB
import com.david.movie.notwork.dto.isValid
import com.david.movie.notwork.dto.isValidActor

object Mapper {

    fun mapTMDBMovieListToMovieItemList(movieList: MovieList?): List<MovieItem> {

        return movieList?.results?.map { movie ->
            MovieItem(
                backdrop_path = movie.backdrop_path,
                id = movie.id,
                original_language = movie.original_language,
                original_title = movie.original_title,
                overview = movie.overview,
                poster_path = movie.poster_path,
                release_date = movie.release_date,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.vote_average
            )
        } ?: emptyList()
    }

    fun mapTMDBMovieItemsToMovieItems(results: List<MovieItemTMDB>?): List<MovieItem> {
        return results?.map { movie ->
            MovieItem(
                backdrop_path = movie.backdrop_path,
                id = movie.id,
                original_language = movie.original_language,
                original_title = movie.original_title,
                overview = movie.overview,
                poster_path = movie.poster_path,
                release_date = movie.release_date,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.vote_average ?: 0.0
            )
        } ?: emptyList()
    }

    fun mapTMDBSimilarMoviesToMovieItems(results: List<SimilarMovieTMDB>?): List<MovieItem> {
        return results?.map { movie ->
            MovieItem(
                backdrop_path = movie.backdrop_path,
                id = movie.id,
                original_language = movie.original_language,
                original_title = movie.original_title,
                overview = movie.overview,
                poster_path = movie.poster_path,
                release_date = movie.release_date,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.vote_average
            )
        } ?: emptyList()
    }


    fun MovieDetailsTMDB.convertToMovieDetailsItem(): MovieDetailsItem = MovieDetailsItem(
        backdrop_path = backdrop_path ?: "",
        id = id.toInt(),
        original_language = original_language,
        original_title = original_title,
        overview = overview ?: "",
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        tagline = tagline ?: "",
        voteAverage = (vote_average),
        genres = genres
    )


    fun mapTMDBCastMembersToActors(castMembers: List<CastMemberTMDB>): List<Actor> {
        return castMembers.filter { it.isValidActor() }
            .map { castMember ->
                Actor(
                    gender = castMember.gender,
                    id = castMember.id,
                    known_for_department = castMember.known_for_department,
                    name = castMember.name,
                    original_name = castMember.original_name,
                    popularity = castMember.popularity,
                    profile_path = castMember.profile_path,
                    cast_id = castMember.cast_id,
                    character = castMember.character,
                    credit_id = castMember.credit_id,
                    order = castMember.order
                )
            }
    }


    fun mapMovieCastCreditToMovieItems(castMembers: List<MovieCastCreditTMDB>?): List<MovieItem> {
        return castMembers?.filter { it.isValid() }?.map { castCredit ->
            MovieItem(
                backdrop_path = castCredit.backdrop_path,
                id = castCredit.id,
                original_language = castCredit.original_language,
                original_title = castCredit.original_title,
                overview = castCredit.overview,
                poster_path = castCredit.poster_path,
                release_date = castCredit.release_date ?: "", // Handle nullable release_date
                title = castCredit.title,
                video = castCredit.video,
                voteAverage = castCredit.vote_average
            )
        } ?: emptyList()
    }

}