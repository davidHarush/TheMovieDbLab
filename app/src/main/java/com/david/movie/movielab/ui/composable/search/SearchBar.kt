package com.david.movie.movielab.ui.composable.search

import androidx.compose.foundation.clickable
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
import com.david.movie.movielab.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    searchable: Searchable,
    hint: String,
) {

    val searchText by searchable.searchText.collectAsState()
    val isSearching by searchable.isSearching.collectAsState()
    val resultsList by searchable.searchResults.collectAsState()


    androidx.compose.material3.SearchBar(
        colors = SearchBarDefaults.colors(
            containerColor = if (isSearching)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer,
            dividerColor = Color.Cyan.copy(alpha = 0.7f),
            inputFieldColors = SearchBarDefaults.inputFieldColors(),
        ),

        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },

        placeholder = { Text(hint) },
        query = searchText,
        onQueryChange = searchable::onSearchQueryChanged,
        onSearch = searchable::onPerformSearch,
        active = isSearching,
        onActiveChange = { searchable.onToggleSearch() },

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (resultsList is UiState.Success && (resultsList as UiState.Success).data.isNotEmpty()) {
            val movies = (resultsList as UiState.Success).data
            LazyColumn {
                items(movies) { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { searchable.onPerformSearch(title) },
                    )
                }
            }
        }
    }
}