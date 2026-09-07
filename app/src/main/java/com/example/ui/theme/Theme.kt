package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = JordanOliveDark,
    onPrimary = JordanOliveOnDark,
    primaryContainer = JordanOliveContainerDark,
    secondary = JordanTerracotta,
    tertiary = JordanLimestone,
    background = JordanBackgroundDark,
    surface = JordanSurfaceDark,
    surfaceVariant = JordanSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = JordanOlivePrimary,
    onPrimary = JordanOliveOnPrimary,
    primaryContainer = JordanOliveContainer,
    onPrimaryContainer = JordanOliveOnContainer,
    secondary = JordanTerracotta,
    secondaryContainer = JordanTerracottaContainer,
    onSecondaryContainer = JordanTerracottaOnContainer,
    tertiary = JordanLimestone,
    tertiaryContainer = JordanLimestoneContainer,
    onTertiaryContainer = JordanLimestoneOnContainer,
    background = JordanCreamBackground,
    surface = JordanSurface,
    surfaceVariant = JordanSurfaceVariant,
    outline = JordanOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted authentic Jordanian theme
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

