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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.david.movie.lab.ui.theme.DarkColorPalette
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(

                colorScheme = DarkColorPalette,
                typography = typography,
                shapes = shapes
            ) {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()


                systemUiController.apply {
                    setStatusBarColor(color = colorScheme.surface)
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
                        item.icon,
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
    val icon: ImageVector,
    val label: String,
    val color: Color,

    ) {
    data object Main :
        BottomNavItem("mainScreen", Icons.Default.Home, "Main", Color.Cyan.copy(alpha = 0.7f))

    data object Favorites : BottomNavItem(
        "favorites",
        Icons.Default.Favorite,
        "Favorites",
        Color.Red.copy(alpha = 0.7f),
    )

    data object Profile :
        BottomNavItem("profile", Icons.Default.Person, "Profile", Color.Green.copy(alpha = 0.7f))
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
