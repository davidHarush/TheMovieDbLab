package com.david.movie.lab.ui.screens.popularPeople

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.david.movie.lab.UiState
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.ui.composable.PagingActorGrid
import com.david.movie.lab.ui.composable.search.AppSearchBar
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen


@Composable
fun PopularPeopleScreen(
    navController: NavController,
    viewModel: PopularPeopleViewModel,
    innerPadding: PaddingValues,

    ) {
    val uiDetailsState by viewModel.personState.collectAsState()

    val navigateBack by viewModel.navigateBack.observeAsState()
    LaunchedEffect(navigateBack) {
        if (navigateBack == true) {
            navController.popBackStack()
        }
    }



    if (uiDetailsState is UiState.Success && (uiDetailsState as UiState.Success).data?.isNotEmpty() == true) {

        BackHandler(enabled = true) {
            viewModel.handleBack()
        }
        val data = (uiDetailsState as UiState.Success).data
        PopularPeople(
            popularPersons = data!!,
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel
        )

    } else {
        when (val state = uiDetailsState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> state.data?.let { data ->
                PopularPeople(
                    popularPersons = data,
                    navController = navController,
                    innerPadding = innerPadding,
                    viewModel = viewModel
                )
            } ?: ErrorScreen("No data available")

            is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
        }
    }

}

@Composable
fun PopularPeople(
    popularPersons: List<Actor>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: PopularPeopleViewModel,
) {
    val persons: LazyPagingItems<Actor> = viewModel.personsPagingData.collectAsLazyPagingItems()

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