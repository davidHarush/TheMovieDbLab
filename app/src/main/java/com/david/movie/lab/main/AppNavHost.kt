package com.david.movie.lab.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.david.movie.lab.ui.screens.main.MainTabScreen
import com.david.movie.lab.ui.screens.main.MainViewModel
import com.david.movie.lab.ui.screens.movieDetails.MovieDetailsScreen
import com.david.movie.lab.ui.screens.personDetails.PersonDetailsScreen
import com.david.movie.lab.ui.screens.popularPeople.PopularPeopleScreen
import com.david.movie.lab.ui.screens.popularPeople.PopularPeopleViewModel


// Utility to extract arguments safely
fun NavBackStackEntry.argument(key: String, default: String = "0"): String =
    arguments?.getString(key) ?: default

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Destinations.MainScreen) {
        composable(Destinations.MainScreen) {
            // Obtain the MainViewModel from Hilt
            val viewModel: MainViewModel = hiltViewModel()
            MainTabScreen(
                viewModel = viewModel,
                navController = navController,
                innerPadding = innerPadding
            )
        }
        composable(Destinations.MovieDetails) { backStackEntry ->
            MovieDetailsScreen(
                movieId = backStackEntry.argument("movieId").toInt(),
                navController = navController
            )
        }
        composable(Destinations.PersonDetails) { backStackEntry ->
            PersonDetailsScreen(
                personId = backStackEntry.argument("personId").toInt(),
                navController = navController
            )
        }
        composable(Destinations.Discover) {
            FavoritesScreen()
        }
        composable(Destinations.PopularPeople) {
            val viewModel: PopularPeopleViewModel = hiltViewModel()
            PopularPeopleScreen(
                viewModel = viewModel,
                navController = navController,
                innerPadding = innerPadding
            )
        }
    }
}