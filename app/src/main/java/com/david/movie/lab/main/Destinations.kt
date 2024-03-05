package com.david.movie.lab.main

object Destinations {
    const val MainScreen = "mainScreen"
    const val MovieDetails = "movieDetails/{movieId}"
    const val PersonDetails = "personDetails/{personId}"
    const val Favorites = "favorites"
    const val Profile = "profile"

    // Functions to create routes with arguments
    fun movieDetailsRoute(movieId: String) = "movieDetails/$movieId"
    fun personDetailsRoute(personId: String) = "personDetails/$personId"
}