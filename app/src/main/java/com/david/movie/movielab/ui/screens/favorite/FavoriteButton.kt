package com.david.movie.movielab.ui.screens.favorite

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.david.movie.movielab.repo.model.MovieItem


@Composable
fun <T> FavoriteButton(item: T , isFavorite: Boolean , onFavoriteClick: ((T, Boolean) -> Unit)? = null){

    val bigSize = 45.dp
    val smallSize = 40.dp
    val iconTint by animateColorAsState(
        targetValue = if (isFavorite) Color.Cyan.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.5f),
        animationSpec = tween(durationMillis = 500), label = ""
    )
    val size by animateDpAsState(
        targetValue = if (isFavorite) bigSize else smallSize,
        animationSpec = tween(durationMillis = 400), label = ""
    )

    Box(modifier = Modifier.height(bigSize), contentAlignment = Alignment.Center) {
        IconButton(
            onClick = {
                onFavoriteClick?.invoke(item, isFavorite)
            },
            modifier = Modifier.size(size)
        ) {
            Crossfade(
                targetState = isFavorite, animationSpec = tween(500),
                label = ""
            ) { targetState ->
                if (targetState) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Add to favorites",
                        tint = iconTint,
                        modifier = Modifier
                            .animateContentSize()
                            .height(bigSize)
                            .width(bigSize)
                    )
                } else {
                    Box (modifier = Modifier.height(smallSize), contentAlignment = Alignment.Center) {

                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .animateContentSize()
                                .height(smallSize)
                                .width(smallSize)
                        )
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Remove from favorites",
                            tint = Color.Cyan.copy(alpha = 0.7f),
                            modifier = Modifier
                                .animateContentSize()
                                .height(smallSize - 10.dp)
                                .width(smallSize - 10.dp)
                        )

                    }
                }
            }
        }
    }
}