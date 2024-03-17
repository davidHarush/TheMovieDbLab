package com.david.movie.lab.ui.screens.popularPeople

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.Destinations
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.ui.composable.ActorGrid
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen


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

    Box(modifier = Modifier.fillMaxSize()) {

        ActorGrid(
            topSpace = 110.dp,
            actors = popularPersons,
            onActorClick = { id ->
                navController.navigate(Destinations.personDetailsRoute(personId = id.toString()))
        })
        SearchBar(viewModel = viewModel, query = "", onQueryChanged = {}, onSearch = {})


    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: PopularPeopleViewModel,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val personList by viewModel.searchPersonPreview.collectAsState()


    androidx.compose.material3.SearchBar(
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isSearching) 0.8f else 0.5f),
            dividerColor = Color.Black,
            inputFieldColors = SearchBarDefaults.inputFieldColors(),
        ),

        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },

        placeholder = { Text("Search People") },
        query = searchText,
        onQueryChange = viewModel::onPreviewText,
        onSearch = viewModel::onSearchText,
        active = isSearching,
        onActiveChange = { viewModel.onToggleSearch() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (personList is UiState.Success && (personList as UiState.Success).data.isNotEmpty()) {
            val movies = (personList as UiState.Success).data
            LazyColumn {
                items(movies) { movieTitle ->
                    Text(
                        text = movieTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp).
                            clickable { viewModel.onSearchText(movieTitle) },

                    )
                }
            }
        }
    }
}



