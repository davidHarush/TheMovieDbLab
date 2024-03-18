package com.david.movie.lab.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            color = Color.Transparent,
                            darkIcons = false
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
                    if (navController.currentDestination?.route == AppRoutes.MainScreen) {
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
    return listOf(
        AppRoutes.Search,
        AppRoutes.MainScreen,
        AppRoutes.PopularPeople,
        AppRoutes.Settings
    ).contains(route)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Discover,
        BottomNavItem.Main,
        BottomNavItem.PopularPeople,
        BottomNavItem.Settings
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
        BottomNavItem(
            AppRoutes.MainScreen,
            R.drawable.movie,
            "Movies",
            Color.Cyan.copy(alpha = 0.7f)
        )

    data object Discover : BottomNavItem(
        AppRoutes.Search,
        R.drawable.search,
        "search",
        Color.Cyan.copy(alpha = 0.7f),
    )

    data object PopularPeople :
        BottomNavItem(
            AppRoutes.PopularPeople,
            R.drawable.account_circle,
            "People",
            Color.Cyan.copy(alpha = 0.7f)
        )

    data object Settings :
        BottomNavItem(
            AppRoutes.Settings,
            R.drawable.settings,
            "Settings",
            Color.Cyan.copy(alpha = 0.7f)
        )

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, innerPadding: PaddingValues) {
    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to TheMovieLab App!",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 30.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(26.dp),
                    color = Color.Cyan.copy(alpha = 0.7f),
                    thickness = 3.dp
                )
                Text(
                    text = "This app is designed for learning purposes and may not fully function. \n\n" +
                            "Some of the buttons might not be active or fully implemented yet. \n\n" +
                            "We invite you to explore and enjoy the features that are existing. \n\n" +
                            //"Your exploration and feedback are crucial for our development process and help us improve.\n\n"+
                            "We using TheMovieDB API to fetch the data and images. \n\n",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        color = Color.LightGray
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Image(
                    painter = painterResource(R.drawable.themovie_blue_short),
                    contentDescription = "App Logo", modifier = Modifier.fillMaxWidth()
                )

            }

        }
    )
}

