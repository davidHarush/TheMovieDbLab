package com.david.movie.lab.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

val DarkColorPalette = darkColorScheme(
    primary = AppColor.DarkBlueTMDB,
    secondary = AppColor.LightBlueTMDB,
    background = AppColor.SoftBlack,
    surface = AppColor.TwilightBlue,
    error = Color(0xFFCF6679),
    onPrimary = AppColor.OffWhite,
    onSecondary = AppColor.DarkBlueTMDB,
    onBackground = AppColor.OffWhite,
    onSurface = AppColor.LightGreenTMDB,
    onError = AppColor.SoftBlack
)

@Composable
fun TheMovieLabTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor -> {
//            val context = LocalContext.current
//            dynamicDarkColorScheme(context)
//        }
//
//        else -> DarkColorPalette
//    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkColorPalette.primary.toArgb()
            //WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = Typography,
        content = content
    )
}