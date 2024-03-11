package com.david.movie.lab.ui.screens.discover

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.ui.composable.ActionButton
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsGrid
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.notwork.dto.Genre
import com.david.movie.notwork.dto.Genres


@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel,
    innerPadding : PaddingValues,

    )  {
    val uiState by viewModel.genresState.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()

    val discoveredMovies = viewModel.discoveredMovies.collectAsState()


    val conx = LocalContext.current

    if (discoveredMovies.value is UiState.Success
        && (discoveredMovies.value as UiState.Success).data?.results?.isNotEmpty() == true) {
        BackHandler(enabled = true) {
            Toast.makeText(conx, "Back button clicked", Toast.LENGTH_SHORT).show()
            viewModel.handleBackWithNonEmptyCategories()

        }

    }


//    when (val discoveredState = discoveredMovies) {
//        is UiState.Loading -> LoadingScreen()
//        is UiState.Success -> GenresView(
//            popularPersons = state.data!!,
//            selectedGenre = selectedGenre,
//            navController = navController,
//            innerPadding = innerPadding,
//            viewModel = viewModel
//        )
//
//        is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
//    }

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> GenresView(
            popularPersons = state.data!!,
            selectedGenre = selectedGenre,
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel
        )

        is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
    }



}

@Composable
fun GenresView(
    popularPersons: Genres,
    selectedGenre : List<Int>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: DiscoverViewModel
) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(innerPadding)) {
        Text(text = "Genres" , modifier = Modifier.padding(16.dp))
        ChipsGrid(popularPersons.genres.map { ChipsModel(
            title = it.name,
            onClick = {
                viewModel.onSelectedGenre(it)
            },
            isSelected = selectedGenre.contains(it.id)
        ) })

        val conx = LocalContext.current

        AppSpacer(height = 50.dp)
        ActionButton(
            modifier = Modifier.padding(16.dp),
            text = "Discover",
            backgroundColor = Color.Blue.copy(alpha = 0.7f),
            onClick = {
                Toast.makeText(conx, "on-Click button", Toast.LENGTH_SHORT).show()

                viewModel.onDiscoverClicked()
            })
    }
}

