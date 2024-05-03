package com.david.movie.movielab.ui.screens.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.david.movie.movielab.BaseViewModel
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.paging.PagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val movieRepo: PagingRepo) : BaseViewModel() {

    enum class MainPageCategory {
        Popular, TopRated, NowPlaying , MyFavorite
    }


    private val _selectedChipIndex = MutableStateFlow(0)
    val selectedChipIndex: StateFlow<Int> = _selectedChipIndex.asStateFlow()

    private val actionState = MutableStateFlow(MainPageCategory.Popular)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviePagingData: Flow<PagingData<MovieItem>> = actionState.flatMapLatest { state ->
        when (state) {
            MainPageCategory.Popular -> {
                movieRepo.getPopularMovieStream().cachedIn(viewModelScope)
            }

            MainPageCategory.TopRated -> {
                movieRepo.getTopRatedMovieStream().cachedIn(viewModelScope)
            }

            else ->{ //MainPageCategory.NowPlaying -> {
                movieRepo.getNowPlayingMovieStream().cachedIn(viewModelScope)
            }

//            else -> {
//                flowOf(PagingData.empty())
//            }
        }
    }

    fun setSelectedChipIndex(newIndex: Int) {
        _selectedChipIndex.value = newIndex
        when (newIndex) {
            0 -> actionState.update { MainPageCategory.Popular }
            1 -> actionState.update { MainPageCategory.TopRated }
            2 -> actionState.update { MainPageCategory.NowPlaying }
            3 -> actionState.update { MainPageCategory.MyFavorite }
        }
    }

    fun refreshFavorites() {
        if (_selectedChipIndex.value == 3) {  // Assuming '3' is the index for MyFavorite tab
            actionState.value = MainPageCategory.MyFavorite
        }
    }
}