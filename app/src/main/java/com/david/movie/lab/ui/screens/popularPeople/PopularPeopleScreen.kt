package com.david.movie.lab.ui.screens.popularPeople

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.Destinations
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.ActorAvatar
import com.david.movie.lab.ui.composable.ActorGrid
import com.david.movie.lab.ui.composable.ActorsList
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.lab.ui.screens.main.MainViewModel
import com.david.movie.lab.ui.screens.main.StaggeredMovieGrid
import com.david.movie.lab.ui.screens.personDetails.PersonDetailViewModel
import com.david.movie.notwork.dto.PopularPersonList


@Composable
fun PopularPeopleScreen(
    navController: NavController,
    viewModel: PopularPeopleViewModel,
    innerPadding : PaddingValues,

    )  {
    val uiDetailsState by viewModel.personState.collectAsState()


    when (val state = uiDetailsState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> PopularPeople(
            popularPersons = state.data!!,
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel
        )

        is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
    }


//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text(text = "Profile")
//        ActorsList(actors = emptyList(), onActorClick = {})
//
//    }
}

@Composable
fun PopularPeople(
    popularPersons : List<Actor>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: PopularPeopleViewModel,
    ) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ActorGrid(
            actors = popularPersons,
            onActorClick = { id ->
                navController.navigate(Destinations.personDetailsRoute(personId = id.toString()))
        })

    }

}
