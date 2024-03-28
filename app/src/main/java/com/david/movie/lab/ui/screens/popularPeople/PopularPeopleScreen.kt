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
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.ui.composable.ActorGrid
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
//https://api.themoviedb.org/3/person/popular?api_key=56a778f90174e0061b6e7c69a5e3c9f2&language=en-US&page=1
//https://api.themoviedb.org/3/person/popular

@Composable
fun PopularPeopleScreen(
    navController: NavController,
    viewModel: PopularPeopleViewModel,
    innerPadding: PaddingValues,

    ) {
    val uiDetailsState by viewModel.personState.collectAsState()


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

@Composable
fun PopularPeople(
    popularPersons: List<Actor>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: PopularPeopleViewModel,
) {

    Box(modifier = Modifier.fillMaxSize()) {

        ActorGrid(
            topSpace = 110.dp,
            actors = popularPersons,
            onActorClick = { id ->
                navController.navigate(AppRoutes.personDetailsRoute(personId = id.toString()))
            })
        SearchBar(viewModel = viewModel,)


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: PopularPeopleViewModel,
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
                items(movies) { name ->
                    Text(
                        text = name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { viewModel.onSearchText(name ?: "") },

                        )
                }
            }
        }
    }
}


//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(
//    viewModel: PopularPeopleViewModel,
//) {
//    val searchText by viewModel.searchText.collectAsState()
//    val isSearching by viewModel.isSearching.collectAsState()
//    val searchResults by viewModel.searchResults.collectAsState()
//
//    androidx.compose.material3.SearchBar(
//        colors = SearchBarDefaults.colors(
//            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isSearching) 0.8f else 0.5f),
//            dividerColor = Color.Black,
//            inputFieldColors = SearchBarDefaults.inputFieldColors(),
//        ),
//        leadingIcon = {
//            Icon(Icons.Filled.Search, contentDescription = "Search")
//        },
//        placeholder = { Text("Search People") },
//        query = searchText,
//        onQueryChange = viewModel::debounceSearch,
//        onSearch = viewModel::performSearch,
//        active = isSearching,
//        onActiveChange = { viewModel.onToggleSearch() },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        // Display search results if available
//        if (searchResults is UiState.Success) {
//            if((searchResults as UiState.Success<List<Actor>>).data.isEmpty()){
//                Text("No results found")
//            }
//            val people = (searchResults as UiState.Success<List<Actor>>).data
//            LazyColumn {
//                items(people) { person ->
//                    Text(
//                        text = person.name ?: "",
//                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clickable { /* Handle click, e.g., navigate to person details */ }
//                    )
//                }
//            }
//        }
//    }
//}




