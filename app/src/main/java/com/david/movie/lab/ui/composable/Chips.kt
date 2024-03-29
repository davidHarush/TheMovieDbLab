package com.david.movie.lab.ui.composable

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


class ChipsModel(
    var title: String = "",
    var onClick: (() -> Unit)? = null,
    var isSelected: Boolean = false
)

@Composable
fun ChipsRow(
    chipList: List<ChipsModel>,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chipList.forEach { chip ->
            Chip(chipModel = chip, isSelected = false, onSelect = { })
        }
    }

}


fun List<ChipsModel>.toChunkedList(chunkSize: Int = 3): List<List<ChipsModel>> {
    return this.chunked(chunkSize)
}

@Composable
fun ChipsGrid(chipList: List<ChipsModel>) {
    // Transform the flat list into a list of lists, each with up to 3 items
    val columns = chipList.chunked(3)

    LazyRow(
        // Horizontal arrangement to space between the columns
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // Apply padding as needed
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(columns.size) { index ->
            // Get the specific column from the list
            val column = columns[index]
            Column(
                // Vertical arrangement to space between the chips
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                column.forEach { chip ->
                    Box(modifier = Modifier.padding(3.dp)) {
                        Chip(
                            chipModel = chip,
                            isSelected = chip.isSelected, // Assuming an isSelected flag for UI logic
                            onSelect = { chip.onClick?.invoke() }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TabsChips(
    chipList: List<ChipsModel>,
    selectedChipIndex: Int,
    onChipSelected: (Int) -> Unit, // Callback when a new chip is selected
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            chipList.forEachIndexed { index, chip ->
                Chip(
                    chipModel = chip,
                    isSelected = index == selectedChipIndex, // Check if the chip is selected
                    onSelect = { onChipSelected(index) } // Update the selection state
                )
                if (index < chipList.size - 1) {
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}


@Composable
fun Chip(
    chipModel: ChipsModel,
    isSelected: Boolean = false,
    onSelect: (() -> Unit),
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isSelected) {
        Color.Black
    } else {
       Color.White
    }

    val chipModifier = Modifier
        .clip(RoundedCornerShape(50))
        .clickable(onClick = {
            Log.d("Chip", "Chip: ${chipModel.title}")
            //chipModel.onClick?.invoke() // If you have additional onClick logic in the model
            onSelect.invoke()
        })
        .padding(horizontal = 8.dp)

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor.copy(alpha = 0.8f),
        modifier = chipModifier
    ) {
        Text(
            text = chipModel.title,
            modifier = Modifier.padding(8.dp),
            color = textColor
        )
    }
}
