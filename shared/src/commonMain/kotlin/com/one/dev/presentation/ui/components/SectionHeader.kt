package com.one.dev.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.JetBrainsMono

@Composable
fun SectionHeader(
    title    : String,
    subtitle : String?  = null,
    modifier : Modifier = Modifier,
) {
    val accentColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier
            .drawBehind {
                drawLine(accentColor, Offset(0f, 0f), Offset(0f, size.height), strokeWidth = 4.dp.toPx())
            }
            .padding(start = Dimens.md),
    ) {
        Text("./ $title", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.xs))
        }
    }
}
