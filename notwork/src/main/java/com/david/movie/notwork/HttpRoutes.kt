package com.david.movie.notwork

object HttpRoutes {
    private const val BASE_URL: String = "https://api.themoviedb.org/3"
    private const val API_KEY = "56a778f90174e0061b6e7c69a5e3c9f2"


    private fun withApiKey(path: String) = "$path?api_key=$API_KEY&language=en-US"

    object Movies {

        fun popular(page: Int = 1) = withApiKey("$BASE_URL/movie/popular") + "&page=$page"
        fun topRated(page: Int = 1) = withApiKey("$BASE_URL/movie/top_rated") + "&page=$page"

        // movie/{movie_id}
        fun details(movieId: Int) = withApiKey("$BASE_URL/movie/$movieId")
        // movie/{movie_id}/credits
        fun credits(movieId: Int) = withApiKey("$BASE_URL/movie/$movieId/credits")

        // https://api.themoviedb.org/3/movie/{movie_id}/similar
        fun similar(movieId: Int) = withApiKey("$BASE_URL/movie/$movieId/similar")

        //https://api.themoviedb.org/3/genre/movie/list
        fun genres() = withApiKey("$BASE_URL/genre/movie/list")


//https://api.themoviedb.org/3/discover/movie?api_key=<<api_key>>
// &language=en-US
// &sort_by=popularity.desc
// &include_adult=false
// &include_video=false
// &page=1
// &release_date.gte=2020-01-01
// &vote_average.gte=8
//  &with_genres=28,35,18

        fun discover(page: Int, withGenres: List<Int>?) =  withApiKey("$BASE_URL/discover/movie") + "&page=$page" +
                withGenres?.let { "&with_genres=${it.joinToString(separator = ",")}" }.orEmpty()
    }

    object Person {
        //  person/{person_id}
        fun details(personId: Int) = withApiKey("$BASE_URL/person/$personId")

        // person/{person_id}/external_ids
        fun externalIds(personId: Int) = withApiKey("$BASE_URL/person/$personId/external_ids")

        //https://api.themoviedb.org/3/person/{person_id}/movie_credits
        fun movieCredits(personId: Int) = withApiKey("$BASE_URL/person/$personId/movie_credits")

        //https://api.themoviedb.org/3/person/popular?api_key=56a778f90174e0061b6e7c69a5e3c9f2&language=en-US&page=1
        fun popular(page: Int = 1) = withApiKey("$BASE_URL/person/popular") + "&page=$page"

    }
}


