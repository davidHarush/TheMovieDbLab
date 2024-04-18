package com.david.movie.movielab.ui.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun FavoriteButton() {
    var isFavorite by remember { mutableStateOf(false) }
    val bigSize = 45.dp
    val smallSize = 35.dp
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
            onClick = { isFavorite = !isFavorite },
            modifier = Modifier.size(size)
        ) {
            Crossfade(
                targetState = isFavorite, animationSpec = tween(500),
                label = ""
            ) { targetState ->
                if (targetState) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Remove from favorites",
                        tint = iconTint,
                        modifier = Modifier
                            .animateContentSize()
                            .height(bigSize)
                            .width(bigSize)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Remove from favorites",
                        tint = iconTint,
                        modifier = Modifier
                            .animateContentSize()
                            .height(smallSize)
                            .width(smallSize)
                    )
                }
            }
        }
    }
}