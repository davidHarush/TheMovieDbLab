package com.david.movie.movielab.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.david.movie.movielab.ui.screens.discover.DiscoverScreen
import com.david.movie.movielab.ui.screens.discover.DiscoverViewModel
import com.david.movie.movielab.ui.screens.main.MainTabScreen
import com.david.movie.movielab.ui.screens.main.MainViewModel
import com.david.movie.movielab.ui.screens.movieDetails.MovieDetailsScreen
import com.david.movie.movielab.ui.screens.personDetails.PersonDetailsScreen
import com.david.movie.movielab.ui.screens.popularPeople.PopularPeopleScreen
import com.david.movie.movielab.ui.screens.popularPeople.PopularPeopleViewModel

fun NavBackStackEntry.argument(key: String, default: String = "0"): String =
    arguments?.getString(key) ?: default

@Composable
fun AppNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = AppRoutes.MainScreen) {
        composable(AppRoutes.MainScreen) {
            // Obtain the MainViewModel from Hilt
            MainTabScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }
        composable(AppRoutes.MovieDetails) { backStackEntry ->
            MovieDetailsScreen(
                movieId = backStackEntry.argument("movieId").toInt(),
                navController = navController
            )
        }
        composable(AppRoutes.PersonDetails) { backStackEntry ->
            PersonDetailsScreen(
                personId = backStackEntry.argument("personId").toInt(),
                navController = navController
            )
        }
        composable(AppRoutes.Search) {
            DiscoverScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }
        composable(AppRoutes.PopularPeople) {
            PopularPeopleScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(AppRoutes.Settings) {
            SettingsScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }
    }
}
