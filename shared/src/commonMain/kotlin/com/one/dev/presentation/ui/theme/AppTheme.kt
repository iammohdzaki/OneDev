package com.one.dev.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFontFamilyResolver

// ── Dynamic theme hook ─────────────────────────────────────────────────────────
// Exposes a MutableState<ColorScheme> via CompositionLocal so that an external
// library can swap the color scheme at runtime without restarting the tree.
val LocalAppColorScheme = compositionLocalOf<MutableState<ColorScheme>> {
    error("LocalAppColorScheme not provided")
}

@Composable
fun AppTheme(
    initialColorScheme: ColorScheme = M3DarkColorScheme,
    content: @Composable () -> Unit,
) {
    val colorSchemeState = remember { mutableStateOf(initialColorScheme) }
    val emojiFontFamily = NotoEmoji
    val fontFamilyResolver = LocalFontFamilyResolver.current

    LaunchedEffect(fontFamilyResolver, emojiFontFamily) {
        try {
            fontFamilyResolver.preload(emojiFontFamily)
        } catch (e: Exception) {
            println("Failed to preload NotoEmoji: ${e.message}")
        }
    }

    CompositionLocalProvider(LocalAppColorScheme provides colorSchemeState) {
        MaterialTheme(
            colorScheme = colorSchemeState.value,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}
