package com.david.movie.lab.main

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.david.movie.lab.R
import com.david.movie.lab.ui.theme.TheMovieLabTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.DisposableEffect


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheMovieLabTheme {
                WindowCompat.setDecorFitsSystemWindows(window, false)

                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()


                SideEffect {
                    systemUiController.apply {
                        setStatusBarColor(
                            color = Color.Transparent, // Status bar color set to transparent
                            darkIcons = false // Status bar icons set to dark for visibility
                        )
                    }
                }

                systemUiController.apply {
                    setNavigationBarColor(color = colorScheme.surface)
                }

                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar(navBackStackEntry)) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        AppNavHost(navController = navController, innerPadding = innerPadding)
                    }
                }
                BackHandler(navController = navController)

            }
        }

    }


    @Composable
    fun BackHandler(navController: NavController) {
        val activity = LocalContext.current as? Activity

        // Remember a back press callback and tie its lifecycle to the composable's lifecycle
        val callback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Check if the current destination is in the root destinations
                    if (navController.currentDestination?.route == Destinations.MainScreen) {
                        activity?.finish()
                    } else if (!navController.popBackStack()) {
                        // If popBackStack returns false, there are no entries to pop, so we can finish the activity
                        activity?.finish()
                    }
                }
            }
        }

        DisposableEffect(navController) {
            activity?.onBackInvokedDispatcher?.registerOnBackInvokedCallback(0) { callback.remove() }
            onDispose {
                callback.remove()
            }
        }
    }
}




@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


fun shouldShowBottomBar(navBackStackEntry: NavBackStackEntry?): Boolean {
    val route = navBackStackEntry?.destination?.route
    return listOf(Destinations.Discover,Destinations.MainScreen, Destinations.PopularPeople).contains(route)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Discover,
        BottomNavItem.Main,
        BottomNavItem.PopularPeople
    )
    val currentRoute = currentRoute(navController)

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(item.icon),
                        contentDescription = item.label,
                        tint = if (isSelected) item.color else Color.LightGray
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected) item.color else Color.LightGray
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String,
    val color: Color,

    ) {
    data object Main :
        BottomNavItem(Destinations.MainScreen, R.drawable.movie, "Movies", Color.Cyan.copy(alpha = 0.7f))

    data object Discover : BottomNavItem(
        Destinations.Discover,
        R.drawable.movie,
        "Discover",
        Color.Yellow.copy(alpha = 0.7f),
    )

    data object PopularPeople :
        BottomNavItem(Destinations.PopularPeople, R.drawable.account_circle, "Popular people", Color.Green.copy(alpha = 0.7f))

}
//https://api.themoviedb.org/3/discover/movie?api_key=%3C%3Capi_key%3E%3E
// &language=en-US
// &sort_by=popularity.desc
// &include_adult=false
// &include_video=false
// &page=1
// &release_date.gte=2020-01-01
// &vote_average.gte=8


//https://api.themoviedb.org/3/discover/movie?api_key=<<api_key>>
// &language=en-US
// &sort_by=popularity.desc
// &include_adult=false
// &include_video=false
// &page=1
// &release_date.gte=2020-01-01
// &vote_average.gte=8
//  &with_genres=28,35,18


@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Favorites")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Profile")
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(50.dp)
        ) {
        }


    }
}
