package com.david.movie.lab.ui.composable


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.david.movie.lab.ui.theme.AppColor

@Composable
fun AppSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = text)
        AppSpacer(width = 16.dp)
        Switch(
            checked = checked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppColor.LightGreenTMDB.copy(alpha = 0.9f),
                checkedTrackColor = AppColor.TwilightBlue.copy(alpha = 0.4f),
                checkedBorderColor = AppColor.TwilightBlue,
                uncheckedThumbColor = AppColor.LightGreenTMDB.copy(alpha = 0.5f),
                uncheckedTrackColor = AppColor.TwilightBlue.copy(alpha = 0.4f),
                uncheckedBorderColor = AppColor.TwilightBlue,
            ),
            onCheckedChange = onCheckedChange
        )
    }
}