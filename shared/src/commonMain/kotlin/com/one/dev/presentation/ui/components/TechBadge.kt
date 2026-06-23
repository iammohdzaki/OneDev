package com.one.dev.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.JetBrainsMono

enum class BadgeVariant { FILLED, OUTLINE }

@Composable
fun TechBadge(
    text    : String,
    variant : BadgeVariant = BadgeVariant.FILLED,
    modifier: Modifier = Modifier,
) {
    val bg = if (variant == BadgeVariant.FILLED) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent
    val textColor = MaterialTheme.colorScheme.primary
    val borderMod = if (variant == BadgeVariant.OUTLINE)
        Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp)) else Modifier
    Text(
        text       = text,
        style      = MaterialTheme.typography.labelSmall,
        color      = textColor,
        fontFamily = JetBrainsMono,
        modifier   = modifier.then(borderMod)
            .clip(RoundedCornerShape(2.dp))
            .background(bg)
            .padding(horizontal = Dimens.badgeHorizontal, vertical = Dimens.badgeVertical),
    )
}
