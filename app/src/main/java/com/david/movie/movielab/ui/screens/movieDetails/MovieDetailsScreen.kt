package com.david.movie.movielab.ui.screens.movieDetails

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.david.movie.movielab.UiState
import com.david.movie.movielab.main.AppRoutes
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.FullMovieCollection
import com.david.movie.movielab.repo.model.MovieDetailsItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.model.getPosterUrl
import com.david.movie.movielab.repo.model.getReleaseYear
import com.david.movie.movielab.ui.composable.ActorsList
import com.david.movie.movielab.ui.composable.AppButtons
import com.david.movie.movielab.ui.composable.AppSpacer
import com.david.movie.movielab.ui.composable.ChipsModel
import com.david.movie.movielab.ui.composable.ChipsRow
import com.david.movie.movielab.ui.composable.RatingProgress
import com.david.movie.movielab.ui.composable.ReadMore
import com.david.movie.movielab.ui.composable.ScrollingContent
import com.david.movie.movielab.ui.composable.SmallMovieRow
import com.david.movie.movielab.ui.screens.ErrorScreen
import com.david.movie.movielab.ui.screens.LoadingScreen
import com.david.movie.movielab.ui.screens.favorite.FavoriteButton
import com.david.movie.movielab.ui.screens.favorite.FavoriteViewModel
import com.david.movie.movielab.ui.theme.AppColor
import com.david.movie.notwork.dto.ImagesResponseTMDB
import com.david.movie.notwork.dto.getFullImageUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    detailsViewModel: MovieDetailsViewModel = hiltViewModel(),

    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, // Start hidden
        skipHalfExpanded = true,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 200,
            easing = FastOutSlowInEasing
        ),

        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded } // Avoid half-expanded state
    )

    val moviesImagesState by detailsViewModel.moviesImagesState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        detailsViewModel.init(movieId)
    }

    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Box(modifier = Modifier.fillMaxHeight(0.6f)) {
                SheetContent(imagesState = moviesImagesState)
            }
        },
        sheetElevation = 4.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        MainContent(
            detailsViewModel = detailsViewModel,
            navController = navController,
            bottomSheetState = bottomSheetState,
            coroutineScope = coroutineScope
        )
    }
}


@Composable
fun MainContent(
    detailsViewModel: MovieDetailsViewModel,
    navController: NavHostController,
    bottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {


    val uiDetailsState by detailsViewModel.movieDetailsState.collectAsStateWithLifecycle()
    val uiCastState by detailsViewModel.movieCastState.collectAsStateWithLifecycle()
    val uiSimilarMoviesState by detailsViewModel.similarMoviesState.collectAsStateWithLifecycle()
    val uiCollectionMoviesState by detailsViewModel.collectionMoviesState.collectAsStateWithLifecycle()

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

    val collectionMovies: FullMovieCollection? = if (uiCollectionMoviesState is UiState.Success) {
        (uiCollectionMoviesState as UiState.Success<FullMovieCollection?>).data
    } else {
        null
    }

    when (val state = uiDetailsState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success<MovieDetailsItem> -> MovieDetailsSuccessContent(
            bottomSheetState = bottomSheetState,
            coroutineScope = coroutineScope,
            movieDetails = state.data,
            cast = cast,
            navController = navController,
            similarMovies = similarMovies,
            collectionMovies = collectionMovies,
            detailsViewModel = detailsViewModel
        )

        is UiState.Error -> ErrorScreen(message = state.exception.message ?: "Unknown Error")
    }

}


@Composable
fun SheetContent(imagesState: UiState<ImagesResponseTMDB?>) {
    if (imagesState is UiState.Loading) {
        LoadingScreen()
        return
    } else if (imagesState is UiState.Error) {
        ErrorScreen(message = imagesState.exception.message ?: "Unknown Error")
        return
    }

    val images = (imagesState as UiState.Success<ImagesResponseTMDB?>).data ?: return


    if (images.backdrops.isEmpty()) {
        Text(
            text = "No images found", color = Color.White, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxSize()
                // canter the text
                .background(AppColor.TwilightBlue)
                .padding(6.dp),
        )
        return
    }


    val imageUrlList = images.backdrops.map { it.getFullImageUrl() }

    val aspectRatio = images.backdrops[0].aspect_ratio

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.TwilightBlue)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(imageUrlList.size) { url ->
            BackdropsImage(
                imageUrl =
                imageUrlList[url],
                aspectRatio = aspectRatio,
            )
        }
    }
}


@Composable
fun BackdropsImage(imageUrl: String, aspectRatio: Double) {
    Box(
        modifier = Modifier
            .aspectRatio(aspectRatio.toFloat())
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio.toFloat())
                .fillMaxSize()
                .align(Alignment.TopCenter)
        )
    }
}


@Composable
fun MovieDetailsSuccessContent(
    bottomSheetState: ModalBottomSheetState,
    movieDetails: MovieDetailsItem,
    coroutineScope: CoroutineScope,
    cast: List<Actor>,
    navController: NavHostController,
    similarMovies: List<MovieItem>?,
    collectionMovies: FullMovieCollection?,
    detailsViewModel: MovieDetailsViewModel,

    ) {

    val chipsModelList = movieDetails.genres.map { genre ->
        ChipsModel(title = genre.name)
    }

    ScrollingContent(
        backgroundImageUrl = movieDetails.getPosterUrl(),
        content = {
            AppSpacer(height = 550.dp)
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
            ReadMore(comment = movieDetails.overview) // description in read more style
            AppSpacer(height = 15.dp)
            ButtonsActionRow( // Gallery and Favorite buttons
                bottomSheetState,
                coroutineScope,
                detailsViewModel,
                movieDetails
            )
            AppSpacer(height = 15.dp)
            AppSpacer(height = 16.dp)
            if (collectionMovies != null) {
                SmallMovieRow(
                    movieList = collectionMovies.movies,
                    title = collectionMovies.name,
                    subTitle = collectionMovies.overview,
                    onMovieClick = { movie ->
                        navController.navigate(
                            AppRoutes.movieDetailsRoute(
                                movieId = movie.id.toString()
                            )
                        )
                    },
                    maxItems = 30
                )
            }
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ButtonsActionRow(
    bottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    viewModel: MovieDetailsViewModel,
    movieDetails: MovieDetailsItem,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
) {
    val isFavoriteState: MutableState<Boolean> = remember { mutableStateOf(false) }
    LaunchedEffect(movieDetails.id) {
        isFavoriteState.value = favoriteViewModel.getFavoriteStatus(movieDetails.id)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {

        AppButtons.Gallery(onClick = {
            coroutineScope.launch {
                viewModel.getMovieImages()
                bottomSheetState.show()
            }
        })
        AppSpacer(width = 16.dp)

        FavoriteButton(
            item = movieDetails,
            isFavorite = isFavoriteState.value,
            onFavoriteClick = { movieItem, _ ->
                favoriteViewModel.onFavoriteClick(
                    movie = movieItem,
                    isFavorite = !isFavoriteState.value,
                    lastWatch = System.currentTimeMillis().toString()
                )
                isFavoriteState.value = !isFavoriteState.value

            }
        )

    }
}
