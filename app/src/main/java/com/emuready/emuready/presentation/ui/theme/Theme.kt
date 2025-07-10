package com.emuready.emuready.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.emuready.emuready.presentation.viewmodels.ThemeViewModel

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = Scrim
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    scrim = Scrim
)

@Composable
fun EmuReadyTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val themePreferences by themeViewModel.themePreferences.collectAsState()
    
    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (themePreferences.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }
    
    val colorScheme = when {
        themePreferences.useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            
            // Enhanced status bar styling with proper contrast
            val statusBarColor = if (darkTheme) {
                colorScheme.surface.copy(alpha = 0.95f).compositeOver(colorScheme.background)
            } else {
                colorScheme.surface.copy(alpha = 0.95f).compositeOver(colorScheme.background)
            }
            
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalThemePreferences provides themePreferences
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = com.emuready.emuready.presentation.ui.theme.Shapes,
            content = content
        )
    }
}

// Utility functions for enhanced theming
@Composable
fun Color.withAlpha(alpha: Float): Color = this.copy(alpha = alpha)

@Composable
fun MaterialTheme.extendedColors(): ExtendedColors {
    return if (isSystemInDarkTheme()) {
        ExtendedColors(
            gaming = HighlightPink,
            onGaming = OnPrimaryDark,
            success = SuccessGreen,
            onSuccess = OnPrimaryDark,
            warning = WarningAmber,
            onWarning = OnPrimaryDark,
            info = InfoBlue,
            onInfo = OnPrimaryDark,
            surfaceElevated = SurfaceElevatedDark,
            onSurfaceElevated = OnSurfaceDark,
            glassmorphism = GlassOverlayDark
        )
    } else {
        ExtendedColors(
            gaming = HighlightPink,
            onGaming = OnPrimary,
            success = SuccessGreen,
            onSuccess = OnPrimary,
            warning = WarningAmber,
            onWarning = OnPrimary,
            info = InfoBlue,
            onInfo = OnPrimary,
            surfaceElevated = SurfaceElevated,
            onSurfaceElevated = OnSurface,
            glassmorphism = GlassOverlay
        )
    }
}

data class ExtendedColors(
    val gaming: Color,
    val onGaming: Color,
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
    val surfaceElevated: Color,
    val onSurfaceElevated: Color,
    val glassmorphism: Color
)