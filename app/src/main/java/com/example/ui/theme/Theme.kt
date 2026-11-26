package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TeeTravelColorScheme = lightColorScheme(
    primary = InkNavy,
    onPrimary = PureWhite,
    primaryContainer = CreamWarmTint,
    onPrimaryContainer = InkNavy,
    secondary = CoralRose,
    onSecondary = PureWhite,
    secondaryContainer = CoralSoft,
    onSecondaryContainer = InkNavy,
    tertiary = LavenderPin,
    onTertiary = PureWhite,
    tertiaryContainer = LavenderSoft,
    onTertiaryContainer = InkNavy,
    background = CreamBackground,
    onBackground = InkNavy,
    surface = PureWhite,
    onSurface = InkNavy,
    surfaceVariant = CreamWarmTint,
    onSurfaceVariant = InkNavyMuted,
    outline = SoftBorder
)

@Composable
fun TeeTravelTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TeeTravelColorScheme,
        typography = TeeTravelTypography,
        content = content
    )
}
