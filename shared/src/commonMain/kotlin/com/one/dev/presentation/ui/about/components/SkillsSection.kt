package com.one.dev.presentation.ui.about.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.data.models.CorePhilosophy
import com.one.dev.data.models.SkillBar
import com.one.dev.presentation.ui.components.BadgeVariant
import com.one.dev.presentation.ui.components.TechBadge
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsSection(
    bars: List<SkillBar>,
    chips: List<String>,
    philosophy: List<CorePhilosophy>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        SectionHeader("../skills")
        Spacer(Modifier.height(Dimens.lg))
        bars.forEach { SkillProgressBar(it); Spacer(Modifier.height(Dimens.md)) }
        Spacer(Modifier.height(Dimens.md))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.sm),
            verticalArrangement = Arrangement.spacedBy(Dimens.sm)
        ) {
            chips.forEach { TechBadge(it, variant = BadgeVariant.OUTLINE) }
        }
        Spacer(Modifier.height(Dimens.xl))
        Text(
            text = "Core Philosophy",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Dimens.md))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                .padding(Dimens.md),
        ) {
            // Red, yellow, green window controls
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
                Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF27C93F)))
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                philosophy.forEach { p ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                    ) {
                        if (p.emoji.isNotEmpty()) {
                            Text(
                                text = p.emoji,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(24.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Terminal,
                                contentDescription = null,
                                tint = GitGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = buildAnnotatedString {
                                val boldIdx = p.text.indexOf(p.bold)
                                if (boldIdx >= 0) {
                                    append(p.text.substring(0, boldIdx))
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) { append(p.bold) }
                                    append(p.text.substring(boldIdx + p.bold.length))
                                } else append(p.text)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillProgressBar(skill: SkillBar) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(if (started) skill.percentage / 100f else 0f, tween(1200), label = "skill")
    LaunchedEffect(Unit) { started = true }
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(skill.name, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface, fontFamily = JetBrainsMono)
            Text("${skill.percentage.toInt()}%", style = MaterialTheme.typography.labelMedium, color = GitGreen, fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(MaterialTheme.colorScheme.surface)) {
            Box(Modifier.fillMaxWidth(progress).fillMaxHeight().clip(RoundedCornerShape(2.dp))
                .background(GitGreen))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMono,
                color = GitGreen,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(end = 12.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(GitGreen.copy(alpha = 0.3f))
        )
    }
}
