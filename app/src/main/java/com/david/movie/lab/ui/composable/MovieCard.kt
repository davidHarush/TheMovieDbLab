package com.david.movie.lab.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.david.movie.lab.R
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.repo.model.getBackdropUrl
import com.david.movie.lab.repo.model.getPosterUrl
import kotlin.reflect.KCallable


@Composable
fun SmallMovieRow(
    movieList: List<MovieItem>?,
    title: String,
    onMovieClick: (MovieItem) -> Unit,
    maxItems: Int? =  null,
    onNavigateToShowAllMovies: (() -> Unit)? = null
) {
    movieList?.let {
        var newMovieList :List<MovieItem> = emptyList()
        if(maxItems != null && movieList.size > maxItems) {
            newMovieList = movieList.subList(0, maxItems)
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.Cyan.copy(alpha = 0.5f)
        )

        AppSpacer(height = 8.dp)
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )


        val cardWidth = 300.dp
        val cardHeight = (cardWidth / 3) * 2

        LazyHorizontalGrid(
            rows = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ) {
            items(newMovieList) { movie ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    WideMovieCard(
                        movie = movie,
                        onMovieClick = onMovieClick,
                        cardWidth = cardWidth
                    )
                }
            }
            if(maxItems != null && movieList.size > maxItems) {
                item { ShowAllItem(onNavigateToShowAllMovies) }
            }
        }

    } ?: run {
        Text(
            text = "No movies found",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun ShowAllItem(onNavigateToShowAllMovies: (() -> Unit)?) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(300.dp).width(200.dp)
            .clickable(onClick = { onNavigateToShowAllMovies?.invoke() }),
        shape = RoundedCornerShape(5.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text ( text = "Show All", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center))
            }


    }

}


@Composable
fun MovieCard(
    movie: MovieItem,
    onMovieClick: (MovieItem) -> Unit,
    cardHeight: Dp = 300.dp,
    imageHeight: Dp = 250.dp,
    transparentBackgroundMovieTitle: Boolean = false,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onMovieClick.invoke(movie) }),
        shape = RoundedCornerShape(5.dp),
    ) {
        Box(
            modifier = Modifier
                .height(cardHeight)
        ) {
            Box(
                /** Movie Image */
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                MovieImage(movieUrl = movie.getPosterUrl())
            }

            Box(
                /** Favorite Button */
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd,

                ) {
                FavoriteButton()
            }
            Box(
                /** Movie Title */
                modifier = Modifier
                    .height(cardHeight - imageHeight)
                    .fillMaxWidth()
                    .background(
                        if (transparentBackgroundMovieTitle) Color.Transparent else Color.DarkGray.copy(
                            alpha = 0.8f
                        )
                    )
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = Color.White,
                    textAlign = TextAlign.Center, // This centers the text horizontally
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .height(cardHeight - imageHeight)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun WideMovieCard(
    movie: MovieItem,
    onMovieClick: (MovieItem) -> Unit,
    cardWidth: Dp = 300.dp, // Assuming you want to control the width and calculate height based on that
) {
    // Calculate the height based on the aspect ratio 3:1
    val cardHeight = (cardWidth / 3) * 2

    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(cardWidth)
            .height(cardHeight)
            .clickable(onClick = { onMovieClick.invoke(movie) }),
        shape = RoundedCornerShape(5.dp),
    ) {
        Box {
            MovieImage(
                movieUrl = movie.getBackdropUrl(),
                modifier = Modifier.fillMaxSize() // The image should fill the card
            )

            Box(
                /** Movie Title */
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.DarkGray.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = Color.White,
                    maxLines = 1, // Ensure the title is only one line and ellipsize if too long
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun MovieImage(movieUrl: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = movieUrl,
        error = painterResource(R.drawable.error_outline)
    )
    Image(
        painter = painter,
        contentDescription = "Movie Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()

    )
}