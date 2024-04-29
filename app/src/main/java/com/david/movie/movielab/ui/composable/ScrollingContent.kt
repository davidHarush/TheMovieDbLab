package com.david.movie.movielab.ui.composable

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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

//
//
//@Composable
//fun ScrollingContent(
//    content: @Composable () -> Unit,
//    backgroundImageUrl: String,
//    fromAlpha: Float = 0.2f,
//    toAlpha: Float = 0.8f,
//) {
//    // Obtain the LocalDensity context for density-aware conversions
//    val density = LocalDensity.current
//
//    // Remember the scroll state to track the scroll progress
//    val scrollState = rememberScrollState()
//
//    // Access the screen height in pixels directly
//    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
//    val maxScrollPx = remember { mutableStateOf(1f) }
//
//    // Calculate the gradient opacity based on the scroll progress
//    val gradientOpacity = remember(scrollState.value, screenHeightPx, maxScrollPx.value) {
//        ((scrollState.value / maxScrollPx.value) * 2).coerceIn(fromAlpha, toAlpha)
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        BackgroundImageWithGradient(
//            imageUrl = backgroundImageUrl,
//            gradientOpacity = gradientOpacity
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .onGloballyPositioned { layoutCoordinates ->
//                    // Update the maximum scroll value after the layout is positioned
//                    maxScrollPx.value =
//                        (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
//                }
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                content()
//            }
//        }
//    }
//}


//@Composable
//fun ScrollingContent(
//    content: @Composable () -> Unit,
//    backgroundImageUrl: String,
//    fromAlpha: Float = 0.2f,
//    toAlpha: Float = 0.8f,
//) {
//    val density = LocalDensity.current
//    val scrollState = rememberScrollState()
//    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
//    val maxScrollPx = remember { mutableFloatStateOf(1f) }
//
//    // Use Animatable for smooth opacity transitions
//    val gradientOpacity = remember { Animatable(fromAlpha) }
//
//    // Update opacity only on actual scroll changes
//    LaunchedEffect(scrollState.value) {
//        // Adjust the factor to reach full opacity at half the maximum scroll distance
//        Log.d("ScrollingContent", "scrollState.value: ${scrollState.value}")
//        val adjustedScrollValue =
//            (scrollState.value / (maxScrollPx.floatValue)).coerceAtLeast(0f).coerceAtMost(1f)
//        Log.d("ScrollingContent", "adjustedScrollValue: $adjustedScrollValue")
//        val targetOpacity =
//            ((adjustedScrollValue.pow(3)) * (toAlpha - fromAlpha) + 2 * fromAlpha).coerceIn(
//                fromAlpha,
//                toAlpha
//            )
//        Log.d("ScrollingContent", "targetOpacity: $targetOpacity")
//        Log.d("ScrollingContent", "gradientOpacity.value: ${gradientOpacity.value}")
//        Log.d("ScrollingContent", "___________________________")
//        gradientOpacity.animateTo(targetOpacity, animationSpec = tween(durationMillis = 500))
//    }
//
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        BackgroundImageWithGradient(
//            imageUrl = backgroundImageUrl,
//            gradientOpacity = gradientOpacity.value
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .onGloballyPositioned { layoutCoordinates ->
//                    maxScrollPx.value =
//                        (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
//                }
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                content()
//            }
//        }
//    }
//}
//
//
//
//
//@Composable
//fun ScrollingContent(
//    content: @Composable () -> Unit,
//    backgroundImageUrl: String,
//    fromAlpha: Float = 0.2f,
//    toAlpha: Float = 0.8f,
//) {
//    val density = LocalDensity.current
//    val scrollState = rememberScrollState()
//    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
//    val maxScrollPx = remember { mutableFloatStateOf(1f) }
//
//    val gradientOpacity = remember { Animatable(fromAlpha) }
//
//    LaunchedEffect(scrollState.value) {
//        val scrollFraction = (scrollState.value / maxScrollPx.floatValue).coerceIn(0f, 1f)
//        val adjustedScrollValue =
//            scrollFraction.pow(3)  // Consider adjusting the exponent based on desired responsiveness
//        val targetOpacity =
//            ((adjustedScrollValue * (toAlpha - fromAlpha) + 2 * fromAlpha)).coerceIn(
//                2 * fromAlpha,
//                toAlpha
//            )
//        gradientOpacity.animateTo(targetOpacity, animationSpec = tween(durationMillis = 50))
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        BackgroundImageWithGradient(
//            imageUrl = backgroundImageUrl,
//            gradientOpacity = gradientOpacity.value
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .onGloballyPositioned { layoutCoordinates ->
//                    maxScrollPx.floatValue =
//                        (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
//                }
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                content()
//            }
//        }
//    }
//}

//
//@Composable
//fun ScrollingContent(
//    content: @Composable () -> Unit,
//    backgroundImageUrl: String,
//    fromAlpha: Float = 0.2f,
//    toAlpha: Float = 0.8f,
//) {
//    val density = LocalDensity.current
//    val scrollState = rememberScrollState()
//    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
//    val maxScrollPx = remember { mutableFloatStateOf(1f) }
//
//    val gradientOpacity = remember { Animatable(fromAlpha) }
//
//    // Watch scroll changes and maxScrollPx to recalculate opacity
//    LaunchedEffect(scrollState.value, maxScrollPx.floatValue) {
//        val scrollFraction = (scrollState.value / (maxScrollPx.floatValue / 2)).coerceIn(0f, 1f)
//        val targetOpacity =
//            (scrollFraction * (toAlpha - fromAlpha) + fromAlpha).coerceIn(fromAlpha, toAlpha)
//        gradientOpacity.animateTo(targetOpacity, animationSpec = tween(durationMillis = 10))
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        BackgroundImageWithGradient(
//            imageUrl = backgroundImageUrl,
//            gradientOpacity = gradientOpacity.value
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .fillMaxWidth()
//                .verticalScroll(scrollState)
//                .onGloballyPositioned { layoutCoordinates ->
//                    maxScrollPx.floatValue =
//                        (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
//                }
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                content()
//            }
//        }
//    }
//}


@Composable
fun ScrollingContent(
    content: @Composable () -> Unit,
    backgroundImageUrl: String,
    fromAlpha: Float = 0.2f,
    toAlpha: Float = 0.8f,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val maxScrollPx = remember { mutableStateOf(1f) }

    val gradientOpacity = remember { Animatable(fromAlpha) }

    // Use snapshotFlow to emit scroll position changes
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollValue ->
                val scrollFraction = (scrollValue / (maxScrollPx.value / 2)).coerceIn(0f, 1f)
                val targetOpacity = (scrollFraction * (toAlpha - fromAlpha) + fromAlpha).coerceIn(
                    fromAlpha,
                    toAlpha
                )
                gradientOpacity.animateTo(
                    targetOpacity,
                    animationSpec = tween(durationMillis = 300)
                )
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImageWithGradient(
            imageUrl = backgroundImageUrl,
            gradientOpacity = gradientOpacity.value
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .onGloballyPositioned { layoutCoordinates ->
                    maxScrollPx.value =
                        (layoutCoordinates.size.height - screenHeightPx).coerceAtLeast(1f)
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
                .background(Color.Black.copy(alpha = gradientOpacity))
        )

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