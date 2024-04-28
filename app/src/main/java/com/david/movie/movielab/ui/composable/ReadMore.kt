package com.david.movie.movielab.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ReadMore(comment: String) {

    val minimumLineLength = 13   //Change this to your desired value

    val expandedState = remember { mutableStateOf(false) }
    val showReadMoreButtonState = remember { mutableStateOf(false) }
    val maxLines = if (expandedState.value) 1000 else minimumLineLength

    Column {
        Text(
            text = comment,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                if (textLayoutResult.lineCount > minimumLineLength - 1) {
                    if (textLayoutResult.isLineEllipsized(minimumLineLength - 1)) showReadMoreButtonState.value =
                        true
                }
            }
        )
        if (showReadMoreButtonState.value) {
            Text(
                text = if (expandedState.value) "Read Less" else "Read More",
                color = Color.Gray,
                modifier = Modifier.clickable {
                    expandedState.value = !expandedState.value
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.LightGray,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}