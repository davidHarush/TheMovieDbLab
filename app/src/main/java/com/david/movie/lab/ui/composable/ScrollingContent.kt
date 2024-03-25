package com.david.movie.lab.ui.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter


@Composable
fun ScrollingContent(
    content: @Composable () -> Unit,
    backgroundImageUrl: String,
    fromAlpha: Float = 0.2f,
    toAlpha: Float = 0.8f,
) {
    // Obtain the LocalDensity context for density-aware conversions
    val density = LocalDensity.current

    // Remember the scroll state to track the scroll progress
    val scrollState = rememberScrollState()

    // Access the screen height in pixels directly
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val maxScrollPx = remember { mutableStateOf(1f) }

    // Calculate the gradient opacity based on the scroll progress
    val gradientOpacity = remember(scrollState.value, screenHeightPx, maxScrollPx.value) {
        (scrollState.value / maxScrollPx.value).coerceIn(fromAlpha, toAlpha)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImageWithGradient(imageUrl = backgroundImageUrl, gradientOpacity = gradientOpacity)

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .onGloballyPositioned { layoutCoordinates ->
                    // Update the maximum scroll value after the layout is positioned
                    maxScrollPx.value = (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
                }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun BackgroundImageWithGradient(
    imageUrl: String,
    gradientOpacity: Float,
) {
    Log.d("BackgroundImageWithGradient", "BackgroundImageWithGradient: $gradientOpacity")

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
                .background( Color.Black.copy(alpha = gradientOpacity)))

        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

    }
}