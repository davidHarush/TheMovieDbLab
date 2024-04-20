package com.david.movie.movielab.ui.screens.popularPeople

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.david.movie.movielab.main.AppRoutes
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.ui.composable.PagingActorGrid
import com.david.movie.movielab.ui.composable.search.AppSearchBar
import com.david.movie.movielab.ui.screens.ErrorScreen
import com.david.movie.movielab.ui.screens.LoadingScreen


@Composable
fun PopularPeopleScreen(
    navController: NavController,
    viewModel: PopularPeopleViewModel,
    innerPadding: PaddingValues,

    ) {
    val personsPagingState: LazyPagingItems<Actor> =
        viewModel.personsPagingData.collectAsLazyPagingItems()


    val navigateBack by viewModel.navigateBack.observeAsState()
    LaunchedEffect(navigateBack) {
        if (navigateBack == true) {
            navController.popBackStack()
        }
    }

    BackHandler(enabled = personsPagingState.itemSnapshotList.isNotEmpty()) {
        Log.d("BackHandler", "BackHandler")
        viewModel.handleBack()
        personsPagingState.refresh()
        return@BackHandler
    }

    when (personsPagingState.loadState.refresh) {
        is LoadState.Loading -> {
            // Show a loading UI
            LoadingScreen()
        }

        is LoadState.Error -> {
            // Show an error UI
            val error = (personsPagingState.loadState.refresh as LoadState.Error).error
            ErrorScreen(error.localizedMessage ?: "An error occurred")
        }

        is LoadState.NotLoading -> {
            // Show the list of persons
            if (personsPagingState.itemCount > 0) {
                PopularPeople(
                    persons = personsPagingState,
                    navController = navController,
                    innerPadding = innerPadding,
                    viewModel = viewModel
                )
            } else {
                // Show a UI for empty list
                LoadingScreen()
            }
        }
    }


}

@Composable
fun PopularPeople(
    persons: LazyPagingItems<Actor>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: PopularPeopleViewModel,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.zIndex(2f)) {
            AppSearchBar(searchable = viewModel, hint = "Search People")
        }

        PagingActorGrid(
            topSpace = 110.dp,
            actors = persons,
            onActorClick = { id ->
                navController.navigate(AppRoutes.personDetailsRoute(personId = id.toString()))
            })

    }

}