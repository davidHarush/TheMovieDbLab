package com.david.movie.lab.ui.composable.search

import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.runIoCoroutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class SearchableViewModel : BaseViewModel(), Searchable {


    private val _searchMoviesPreview: MutableStateFlow<UiState<List<String>>> =
        MutableStateFlow(UiState.Loading)
    override val searchMoviesPreview = _searchMoviesPreview.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    override val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    override val searchText = _searchText.asStateFlow()


    override val searchResults: StateFlow<UiState<List<String>>>
        get() = _searchMoviesPreview

    override fun onSearchQueryChanged(query: String) {
        _searchText.value = query

        runIoCoroutine {
            val searchResults = onSearchPreview(query)
            _searchMoviesPreview.value = UiState.Success(searchResults)
        }
    }

    override fun onPerformSearch(query: String) {
        _searchText.value = query
        runIoCoroutine { doSearch(query) }
    }

    override fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchQueryChanged("")
        }
    }

    /**
     * Perform search with the final query and show the results screen
     */
    abstract suspend fun doSearch(query: String)

    /**
     * Perform search with a preview query and show the autocomplete results
     */
    abstract suspend fun onSearchPreview(query: String): List<String>

}