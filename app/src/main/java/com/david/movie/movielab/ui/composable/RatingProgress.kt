package com.david.movie.movielab.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RatingProgress(score: Double) {
    // Ensure the score is within 0.0 to 10.0
    val safeScore = score.coerceIn(0.0, 10.0)
    val percentage = (safeScore * 10).toFloat()

    // Define the colors for the gradient based on the score
    val color = when {
        percentage < 40 -> Color.Red
        percentage < 70 -> Color.Yellow
        else -> Color.Green
    }

    // Draw the circular progress indicator with the rating
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(53.dp)
            .background(
                Color.DarkGray,
                shape = CircleShape
            ) // Adjust the size of the circle as needed
    ) {
        Canvas(modifier = Modifier.size(45.dp)) {
            drawArc(
                color = color.copy(alpha = 0.8f), // Slightly transparent arc
                startAngle = -90f,
                sweepAngle = (360 * percentage) / 100,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Square),
            )
        }
        Text(
            text = "${percentage.toInt()}",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray),
            fontSize = 16.sp
        )
    }
}
