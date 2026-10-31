package com.asta.calculatorapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class Appearance { SYSTEM, LIGHT, DARK }

private fun cherryScheme(dark: Boolean): ColorScheme {
    val primary = Color(if (dark) 0xFFFF9DB5 else 0xFFAF294A)
    val onPrimary = Color(if (dark) 0xFF3B1528 else 0xFFFFFFFF)
    val container = Color(if (dark) 0xFF502A3B else 0xFFF9DEE5)
    val onContainer = Color(if (dark) 0xFFFFD9E3 else 0xFF652039)
    val background = Color(if (dark) 0xFF1C151D else 0xFFFCF8F5)
    val surface = Color(if (dark) 0xFF261D27 else 0xFFFFFCFA)
    val variant = Color(if (dark) 0xFF392A36 else 0xFFF1E7E7)
    val text = Color(if (dark) 0xFFF9E8EE else 0xFF422731)
    val muted = Color(if (dark) 0xFFCAB6C0 else 0xFF78616B)
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(primary = primary, onPrimary = onPrimary,
        primaryContainer = container, onPrimaryContainer = onContainer,
        secondary = primary, onSecondary = onPrimary,
        secondaryContainer = container, onSecondaryContainer = onContainer,
        tertiary = muted, onTertiary = background,
        tertiaryContainer = variant, onTertiaryContainer = text,
        background = background, onBackground = text, surface = surface, onSurface = text,
        surfaceVariant = variant, onSurfaceVariant = muted,
        surfaceTint = primary, inverseSurface = text, inverseOnSurface = background,
        inversePrimary = Color(if (dark) 0xFFAF294A else 0xFFFF9DB5),
        outline = Color(if (dark) 0xFF9B828F else 0xFF927780),
        outlineVariant = Color(if (dark) 0xFF4B3845 else 0xFFE9DDDF),
        surfaceBright = surface, surfaceDim = background,
        surfaceContainerLowest = background, surfaceContainerLow = surface,
        surfaceContainer = surface, surfaceContainerHigh = variant, surfaceContainerHighest = variant)
}

@Composable
fun CalculatorAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val scheme = cherryScheme(darkTheme)
    val view = LocalView.current
    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = scheme, typography = Typography, content = content)
}
