package com.david.movie.movielab.main

object AppRoutes {
    const val MainScreen = "mainScreen"
    const val MovieDetails = "movieDetails/{movieId}"
    const val PersonDetails = "personDetails/{personId}"
    const val PopularPeople = "PopularPeople"
    const val Search = "search"
    const val Favorite = "Favorite"
    const val Settings = "Settings"

    // Functions to create routes with arguments
    fun movieDetailsRoute(movieId: String) = "movieDetails/$movieId"
    fun personDetailsRoute(personId: String) = "personDetails/$personId"
}