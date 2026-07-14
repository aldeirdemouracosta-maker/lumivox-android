package br.com.lumivox.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LumivoxDarkColors = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,
    secondary = NeonCyan,
    onSecondary = Navy950,
    background = Navy950,
    onBackground = TextWhite,
    surface = Navy900,
    onSurface = TextWhite,
    surfaceVariant = Navy800,
    onSurfaceVariant = TextMuted,
    outline = NeonBlue,
    error = Color(0xFFFF6B7A)
)

private val LumivoxHighContrastColors = darkColorScheme(
    primary = Color(0xFF00FFFF),
    onPrimary = Color.Black,
    secondary = Color.Yellow,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF101010),
    onSurfaceVariant = Color.White,
    outline = Color(0xFF00FFFF),
    error = Color(0xFFFF5252)
)

@Composable
fun LumivoxTheme(
    highContrast: Boolean,
    largeText: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (highContrast) LumivoxHighContrastColors else LumivoxDarkColors,
        typography = lumivoxTypography(largeText),
        content = content
    )
}

