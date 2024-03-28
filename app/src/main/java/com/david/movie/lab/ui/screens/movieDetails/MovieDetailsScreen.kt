package com.david.movie.lab.ui.screens.movieDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.MovieDetailsItem
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.repo.model.getPosterUrl
import com.david.movie.lab.repo.model.getReleaseYear
import com.david.movie.lab.ui.composable.ActorsList
import com.david.movie.lab.ui.composable.AppButtons
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.composable.ChipsRow
import com.david.movie.lab.ui.composable.RatingProgress
import com.david.movie.lab.ui.composable.ScrollingContent
import com.david.movie.lab.ui.composable.SmallMovieRow
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
        viewModel.getMovieCast(movieId)
        viewModel.getSimilarMovies(movieId)

    }


    // Collect the UI state
    val uiDetailsState by viewModel.movieDetailsState.collectAsState()

    val uiCastState by viewModel.movieCastState.collectAsState()
    val uiSimilarMoviesState by viewModel.similarMoviesState.collectAsState()


    val cast = if (uiCastState is UiState.Success) {
        (uiCastState as UiState.Success<List<Actor>>).data
    } else {
        emptyList()
    }

    val similarMovies = if (uiSimilarMoviesState is UiState.Success) {
        (uiSimilarMoviesState as UiState.Success<List<MovieItem>?>).data
    } else {
        emptyList()
    }

    when (val state = uiDetailsState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success<MovieDetailsItem> -> MovieDetailsSuccessContent(
            movieDetails = state.data,
            cast = cast,
            viewModel = viewModel,
            navController = navController,
            similarMovies = similarMovies

        )

        is UiState.Error -> ErrorScreen(message = state.exception.message ?: "Unknown Error")
    }

}


@Composable
fun MovieDetailsSuccessContent(
    movieDetails: MovieDetailsItem,
    cast: List<Actor>,
    viewModel: MovieDetailsViewModel,
    navController: NavHostController,
    similarMovies: List<MovieItem>?,

    ) {

    val chipsModelList = movieDetails.genres.map { genre ->
        ChipsModel(title = genre.name)
    }

    ScrollingContent(
        backgroundImageUrl = movieDetails.getPosterUrl(),
        content = {
            AppSpacer(height = 250.dp)
            Text(
                text = movieDetails.title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Release Year: ${movieDetails.getReleaseYear()}",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = Color.LightGray
            )

            AppSpacer(height = 15.dp)
            RatingProgress(score = movieDetails.voteAverage)
            AppSpacer(height = 15.dp)
            ChipsRow(chipList = chipsModelList)
            AppSpacer(height = 15.dp)
            Text(
                text = movieDetails.overview,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            AppSpacer(height = 15.dp)
            ButtonsActionRow()
            AppSpacer(height = 15.dp)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.Cyan.copy(alpha = 0.5f)
            )
            AppSpacer(height = 15.dp)
            Text(
                text = "Cast",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp

            )
            ActorsList(actors = cast, onActorClick = { id ->
                navController.navigate(AppRoutes.personDetailsRoute(personId = id.toString()))
            })
            AppSpacer(height = 16.dp)
            if (similarMovies?.isNotEmpty() == true) {
                SmallMovieRow(
                    movieList = similarMovies,
                    title = "Similar Movies",
                    onMovieClick = { movie ->
                        navController.navigate(
                            AppRoutes.movieDetailsRoute(
                                movieId = movie.id.toString()
                            )
                        )
                    },
                    maxItems = 10
                )
            }

        })

}



@Composable
fun ButtonsActionRow() {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppButtons.Watch()
        AppButtons.Share()
        AppButtons.Favorite()
    }

}


