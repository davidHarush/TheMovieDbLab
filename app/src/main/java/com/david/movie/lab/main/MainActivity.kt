package com.david.movie.lab.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    return listOf("mainScreen", "favorites", "profile").contains(route)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Favorites,
        BottomNavItem.Main,
        BottomNavItem.Profile
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
                        tint = if (isSelected) item.color else Color.DarkGray
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected) item.color else Color.DarkGray
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
        BottomNavItem("mainScreen", R.drawable.movie, "Movies", Color.Cyan.copy(alpha = 0.7f))

    data object Favorites : BottomNavItem(
        "favorites",
        R.drawable.movie,
        "Favorites",
        Color.Red.copy(alpha = 0.7f),
    )

    data object Profile :
        BottomNavItem("profile", R.drawable.account_circle, "Popular people", Color.Green.copy(alpha = 0.7f))
}


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
