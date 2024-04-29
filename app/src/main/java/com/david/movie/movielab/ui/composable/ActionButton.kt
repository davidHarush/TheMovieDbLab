package com.david.movie.movielab.ui.composable

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.david.movie.movielab.R
import com.david.movie.movielab.showToast

object AppButtons {
    @Composable
    fun Share(onClick: @Composable () -> Unit = {}) {
        val context = LocalContext.current
        ActionButton(
            text = "Share",
            icon = Icons.Default.Share,
            backgroundColor = Color(0x883F51B5),
            onClick = {
                context.showToast("Share not implemented yet")
            }
        )
    }

    @Composable
    fun Gallery(onClick: () -> Unit = {}) {
        ActionButton(
            text = "Gallery",
            icon = ImageVector.vectorResource(id = R.drawable.baseline_image_24),
            backgroundColor = Color(0x883F51B5),
            onClick = onClick  // Use the passed onClick lambda here
        )
    }

    @Composable
    fun Watch(onClick: () -> Unit = {}) {
        val context = LocalContext.current
        ActionButton(
            text = "Watch",
            icon = Icons.Default.PlayArrow,
            backgroundColor = Color(0x884CAF50),
            onClick = {
                context.showToast("Watch not implemented yet")
            }
        )
    }

    @Composable
    fun Favorite(onClick: () -> Unit = {}) {
        val context = LocalContext.current
        ActionButton(
            text = "Favorite",
            icon = Icons.Default.Favorite,
            backgroundColor = Color(0x88F44336),
            onClick = {
                context.showToast("Favorite not implemented yet")
            }
        )
    }


    @Composable
    fun ActionButton(
        modifier: Modifier = Modifier,
        text: String = "",
        icon: ImageVector? = null,
        backgroundColor: Color = Color.Transparent,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ) {
        Button(
            enabled = enabled,
            modifier = modifier,
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


}

