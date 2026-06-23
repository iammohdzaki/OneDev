package com.one.dev.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// ── Neon Cyberpunk Color tokens ──────────────────────────────────────────────
val CyberpunkBg = Color(0xFF0C0E10)          // Neutral (page background)
val CyberpunkCardBg = Color(0xFF1A1D21)      // Tertiary (container background)
val CyberpunkBorder = Color(0xFF2A2E35)      // Sleek tech outline

val NeonCyan = Color(0xFF00F0FF)            // Primary
val NeonLime = Color(0xFFADFF2F)            // Secondary
val NeonPink = Color(0xFFFF007F)            // Accent neon pink
val NeonYellow = Color(0xFFFFEA00)          // Accent yellow

val M3OnPrimary = Color(0xFF00363C)
val M3PrimaryContainer = Color(0xFF004E57)
val M3OnPrimaryContainer = Color(0xFFE0FCFF)

val M3OnSecondary = Color(0xFF243B00)
val M3SecondaryContainer = Color(0xFF385500)
val M3OnSecondaryContainer = Color(0xFFF0FFD1)

val M3OnSurface = Color(0xFFF0F2F5)
val M3OnSurfaceVariant = Color(0xFF90A0B5)

val M3DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = CyberpunkBg,
    primaryContainer = Color(0xFF004E57),
    onPrimaryContainer = Color(0xFFE0FCFF),
    inversePrimary = Color(0xFF00606B),

    secondary = NeonLime,
    onSecondary = CyberpunkBg,
    secondaryContainer = Color(0xFF385500),
    onSecondaryContainer = Color(0xFFF0FFD1),

    tertiary = CyberpunkCardBg,
    onTertiary = M3OnSurface,
    tertiaryContainer = CyberpunkCardBg,
    onTertiaryContainer = M3OnSurface,

    background = CyberpunkBg,
    onBackground = M3OnSurface,

    surface = CyberpunkCardBg,
    onSurface = M3OnSurface,
    surfaceVariant = Color(0xFF22262D),
    onSurfaceVariant = M3OnSurfaceVariant,
    surfaceTint = NeonCyan,

    inverseSurface = M3OnSurface,
    inverseOnSurface = CyberpunkBg,

    outline = NeonCyan.copy(alpha = 0.5f),
    outlineVariant = CyberpunkBorder,

    error = NeonPink,
    onError = Color(0xFF3B001B),
    errorContainer = Color(0xFF5C002B),
    onErrorContainer = Color(0xFFFFD9E2),
)

// ── Legacy aliases — mapped to Cyberpunk palette ─────────────────────────────
val BgPrimary = CyberpunkBg
val BgSurface = CyberpunkCardBg
val BgElevated = Color(0xFF22262D)
val GitGreen = NeonLime
val GitBlue = NeonCyan
val GitPurple = NeonPink
val GitOrange = NeonPink
val TextPrimary = M3OnSurface
val TextSecondary = M3OnSurfaceVariant
val BorderColor = CyberpunkBorder

val M3SurfaceContainer = Color(0xFF22262D)
val M3SurfaceContainerLow = CyberpunkCardBg
val M3Tertiary = NeonPink
