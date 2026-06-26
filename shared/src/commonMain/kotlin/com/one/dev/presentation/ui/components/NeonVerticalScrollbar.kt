package com.one.dev.presentation.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NeonVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    if (scrollState.maxValue <= 0) return

    val primaryColor = MaterialTheme.colorScheme.primary // Neon Cyan

    BoxWithConstraints(modifier.fillMaxHeight()) {
        val totalHeight = maxHeight

        // Scroll progress ratios
        val value = scrollState.value.toFloat()
        val maxValue = scrollState.maxValue.toFloat()

        // Thumb height (proportional to viewport, minimum 20% of viewport)
        val thumbHeightRatio = 0.2f
        val thumbHeight = totalHeight * thumbHeightRatio

        // Thumb position
        val scrollRatio = value / maxValue
        val maxScrollTop = totalHeight * (1f - thumbHeightRatio)
        val scrollTop = maxScrollTop * scrollRatio

        // Track background
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(4.dp)
                .background(Color.White.copy(alpha = 0.03f))
        )

        // Neon Glow Thumb
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = scrollTop)
                .width(4.dp)
                .height(thumbHeight)
                .clip(RoundedCornerShape(2.dp))
                .background(primaryColor)
        )
    }
}
