package com.david.movie.notwork

object HttpRoutes {
    private const val BASE_URL: String = "https://api.themoviedb.org/3"
    private const val API_KEY = "56a778f90174e0061b6e7c69a5e3c9f2"


    private fun withApiKeyAndLanguage(path: String) = "$path?api_key=$API_KEY&language=en-US"
    private fun withApiKey(path: String) = "$path?api_key=$API_KEY"


    object Movies {

        fun popular(page: Int = 1) =
            withApiKeyAndLanguage("$BASE_URL/movie/popular") + "&page=$page"

        fun nowPlaying(page: Int = 1) =
            withApiKeyAndLanguage("$BASE_URL/movie/now_playing") + "&page=$page"

        fun topRated(page: Int = 1) =
            withApiKeyAndLanguage("$BASE_URL/movie/top_rated") + "&page=$page"

        // movie/{movie_id}
        fun details(movieId: Int) = withApiKeyAndLanguage("$BASE_URL/movie/$movieId")

        // movie/{movie_id}/credits
        fun credits(movieId: Int) = withApiKeyAndLanguage("$BASE_URL/movie/$movieId/credits")

        //  movie/{movie_id}/similar
        fun similar(movieId: Int) = withApiKeyAndLanguage("$BASE_URL/movie/$movieId/similar")

        //  movie/list
        fun genres() = withApiKeyAndLanguage("$BASE_URL/genre/movie/list")

        //  search/movie
        fun search(query: String, page: Int) =
            withApiKeyAndLanguage("$BASE_URL/search/movie") + "&page=$page&query=$query"

        fun discover(page: Int, withGenres: List<Int>?, rating: Float) =
            withApiKeyAndLanguage("$BASE_URL/discover/movie") + "&page=$page" + "&vote_average.gte=$rating" +
                    withGenres?.let { "&with_genres=${it.joinToString(separator = ",")}" }.orEmpty()

        // movie/{movie_id}/images
        fun images(movieId: Int) = withApiKey("$BASE_URL/movie/$movieId/images")

        // https://api.themoviedb.org/3/collection/{collection_id}
        fun collection(collectionId: Int) =
            withApiKeyAndLanguage("$BASE_URL/collection/$collectionId")
    }

    object Person {
        //  person/{person_id}
        fun details(personId: Int) = withApiKeyAndLanguage("$BASE_URL/person/$personId")

        //  person/{person_id}/external_ids
        fun externalIds(personId: Int) =
            withApiKeyAndLanguage("$BASE_URL/person/$personId/external_ids")

        //  person/{person_id}/movie_credits
        fun movieCredits(personId: Int) =
            withApiKeyAndLanguage("$BASE_URL/person/$personId/movie_credits")

        // person/popular
        fun popular(page: Int = 1) =
            withApiKeyAndLanguage("$BASE_URL/person/popular") + "&page=$page"

        // search/person
        fun search(query: String, page: Int) =
            withApiKeyAndLanguage("$BASE_URL/search/person") + "&page=$page&query=$query"


    }
}


