package com.david.movie.lab.ui.composable

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun BackgroundImageWithGradient(
    imageUrl: String,
    gradientColors: List<Color> = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = Float.POSITIVE_INFINITY
                    )
                )
        )
    }
}


@Composable
fun BackgroundImageWithDynamicGradient(
    imageUrl: String,
) {
    val context = LocalContext.current
    var gradientColors = remember { mutableStateOf(listOf(Color.Black, Color.Black)) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false).listener { _, result ->
                val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    val palette = Palette.from(bitmap).generate()
                    gradientColors.value = listOf(
                        palette.darkVibrantSwatch?.rgb?.let { Color(it) } ?: Color.Black,
                        palette.darkMutedSwatch?.rgb?.let { Color(it) } ?: Color.Black,
                        Color.Black
                    )
                }
            }
            .build()
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors.value,
                        startY = 20f,
                    )
                )
        )

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
    }
}
