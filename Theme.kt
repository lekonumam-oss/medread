package com.example.medread.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Color Palette ──────────────────────────────────────────────────────────────
// Medical blue theme — clean and professional

// Primary blues
val MedBlue = Color(0xFF1565C0)          // Deep medical blue
val MedBlueDark = Color(0xFF0D47A1)      // Darker shade for backgrounds
val MedBlueLight = Color(0xFF42A5F5)     // Lighter shade for accents
val MedBlueContainer = Color(0xFFBBDEFB) // Very light blue for containers

// Secondary / accent
val MedTeal = Color(0xFF00695C)
val MedTealContainer = Color(0xFFB2DFDB)

// Error / alert (also used for PDF icon and filled heart)
val MedRed = Color(0xFFD32F2F)
val MedRedContainer = Color(0xFFFFCDD2)

// Neutrals
val MedWhite = Color(0xFFFFFFFF)
val MedBackground = Color(0xFFF5F8FF)    // Very slightly blue-tinted white background
val MedSurface = Color(0xFFFFFFFF)
val MedOnSurface = Color(0xFF1A1C1E)
val MedOnSurfaceVariant = Color(0xFF44474F)

// ── Light Color Scheme ─────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary = MedBlue,
    onPrimary = MedWhite,
    primaryContainer = MedBlueContainer,
    onPrimaryContainer = MedBlueDark,

    secondary = MedTeal,
    onSecondary = MedWhite,
    secondaryContainer = MedTealContainer,
    onSecondaryContainer = MedTeal,

    error = MedRed,
    onError = MedWhite,
    errorContainer = MedRedContainer,
    onErrorContainer = MedRed,

    background = MedBackground,
    onBackground = MedOnSurface,

    surface = MedSurface,
    onSurface = MedOnSurface,
    onSurfaceVariant = MedOnSurfaceVariant,

    outline = Color(0xFFCDD5E0)
)

// ── Dark Color Scheme ──────────────────────────────────────────────────────────
// Dark mode support — darker blues for a comfortable night-time reading experience
private val DarkColorScheme = darkColorScheme(
    primary = MedBlueLight,
    onPrimary = MedBlueDark,
    primaryContainer = MedBlueDark,
    onPrimaryContainer = MedBlueContainer,

    secondary = Color(0xFF4DB6AC),
    onSecondary = Color(0xFF00363B),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    onSurfaceVariant = Color(0xFFC4C6D0)
)

/**
 * MedReadTheme
 *
 * Wraps the entire app in the MedRead visual theme.
 * Material 3 theming works by wrapping composables in MaterialTheme with
 * a colorScheme (colors), shapes, and typography.
 *
 * Dynamic color (Android 12+): uses the user's wallpaper colors.
 * We disable this to keep the consistent medical blue theme.
 */
@Composable
fun MedReadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to true to use Android 12 dynamic colors (wallpaper-based)
    // We set false to keep our custom medical blue theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Update the status bar color to match our primary blue
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // Typography uses Material 3 defaults — clean and readable
        // You can customize this later in a separate Typography.kt file
        content = content
    )
}
