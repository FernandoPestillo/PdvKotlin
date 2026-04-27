package com.example.pdvkotlin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PdvGreen,
    secondary = PdvBlue,
    tertiary = Color(0xFF22C55E),
    background = PdvDark,
    surface = PdvSurfaceDark,
)

private val LightColorScheme = lightColorScheme(
    primary = PdvGreen,
    secondary = PdvBlue,
    tertiary = Color(0xFF047857),
    background = PdvGray,
    surface = Color.White,
    onSurface = PdvSlate,
)

@Composable
fun PdvKotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
