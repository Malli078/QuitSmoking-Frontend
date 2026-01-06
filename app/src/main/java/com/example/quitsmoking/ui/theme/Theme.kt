package com.example.quitsmoking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    secondary = CyanSecondary,
    background = Color.White,
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = TealPrimary,
    secondary = CyanSecondary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun QuitSmokingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
