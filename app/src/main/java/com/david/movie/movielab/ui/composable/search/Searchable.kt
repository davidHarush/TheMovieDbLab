package com.david.movie.movielab.ui.composable.search

import com.david.movie.movielab.UiState
import kotlinx.coroutines.flow.StateFlow

interface Searchable {
    val searchText: StateFlow<String>
    val isSearching: StateFlow<Boolean>
    val searchResults: StateFlow<UiState<List<String>>>
    val searchMoviesPreview: StateFlow<UiState<List<String>>>


    fun onSearchQueryChanged(query: String)
    fun onPerformSearch(query: String)
    fun onToggleSearch()
}
