package com.david.movie.lab.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object AppButtons {
    @Composable
    fun Share(onClick: () -> Unit = {}) {
        ActionButton(
            text = "Share",
            icon = Icons.Default.Share,
            backgroundColor = Color(0xBF3F51B5), // Blue
            onClick = onClick
        )
    }

    @Composable
    fun Watch(onClick: () -> Unit = {}) {
        ActionButton(
            text = "Watch",
            icon = Icons.Default.PlayArrow,
            backgroundColor = Color(0xBF4CAF50),
            onClick = onClick
        )
    }

    @Composable
    fun Favorite(onClick: () -> Unit = {}) {
        ActionButton(
            text = "Favorite",
            icon = Icons.Default.Favorite,
            backgroundColor = Color(0xBFF44336),
            onClick = onClick
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String = "",
    icon: ImageVector? = null,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = Modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        icon?.let { Icon(icon, contentDescription = text, tint = Color.White) }
        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = Color.White)
        }
    }
}
