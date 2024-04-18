package com.david.movie.movielab.ui.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppSpacer(width: Dp = 0.dp, height: Dp = 0.dp) =
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
    )