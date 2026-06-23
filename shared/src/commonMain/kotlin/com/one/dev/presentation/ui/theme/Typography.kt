package com.one.dev.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import onedev.shared.generated.resources.*
import org.jetbrains.compose.resources.Font

// ── Font families ──────────────────────────────────────────────────────────────
val GoogleSans: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.sans_thin, FontWeight.Thin),
        Font(Res.font.sans_regular, FontWeight.Normal),
        Font(Res.font.sans_medium, FontWeight.Medium),
        Font(Res.font.sans_bold, FontWeight.Bold)
    )

// Packaged JetBrains Mono font resource
val JetBrainsMono: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(Res.font.jetbrains_mono_bold, FontWeight.Bold)
    )

// ── App Typography ─────────────────────────────────────────────────────────────
val AppTypography: Typography
    @Composable
    get() = Typography(
        // Display — Google Sans Display (headline-lg: 48sp / -0.02em / Bold)
        displayLarge = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.02).sp
        ),
        displayMedium = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            letterSpacing = (-0.02).sp
        ),
        displaySmall = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = (-0.01).sp
        ),

        // Headline — Google Sans (headline-md: 28sp / SemiBold)
        headlineLarge = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.02).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        // Title — Google Sans (title-md: 18sp / SemiBold)
        titleLarge = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Body — Google Sans (body-lg: 16sp / Normal, body-md: 14sp / Normal)
        bodyLarge = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),

        // Label — JetBrains Mono for code labels (code-label: 12sp / Medium)
        labelLarge = TextStyle(
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
    )
