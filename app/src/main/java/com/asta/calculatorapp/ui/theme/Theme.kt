package com.asta.calculatorapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FuturisticDarkColorScheme = darkColorScheme(
    primary = NeonCherryPrimary,
    onPrimary = TextBright,
    primaryContainer = CherryContainerDeep,
    onPrimaryContainer = CherryAccentSoft,
    secondary = NeonCherrySecondary,
    onSecondary = DarkBgPrimary,
    secondaryContainer = SciFiGlassPanel,
    onSecondaryContainer = TextBright,
    tertiary = CherryAccentSoft,
    onTertiary = DarkBgPrimary,
    background = DarkBgPrimary,
    onBackground = TextBright,
    surface = DarkBgSecondary,
    onSurface = TextBright,
    surfaceVariant = SciFiGlassPanel,
    onSurfaceVariant = TextMuted,
    error = ErrorBright,
    errorContainer = ErrorContainer,
    onError = TextBright,
    outline = SciFiGlassBorder
)

@Composable
fun CalculatorAppTheme(
    darkTheme: Boolean = true, // Default to futuristic dark neon theme
    dynamicColor: Boolean = false, // Prefer explicit sci-fi cherry aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicDarkColorScheme(context)
        }
        else -> FuturisticDarkColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBgPrimary.toArgb()
            window.navigationBarColor = DarkBgPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
