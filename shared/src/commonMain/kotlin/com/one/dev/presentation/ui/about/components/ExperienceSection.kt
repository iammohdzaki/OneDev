package com.one.dev.presentation.ui.about.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.one.dev.data.models.Experience
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
import org.jetbrains.compose.resources.stringResource
import onedev.shared.generated.resources.*

@Composable
fun ExperienceSection(experiences: List<Experience>, modifier: Modifier = Modifier) {
    Column(modifier) {
        SectionHeader("../experience")
        Spacer(Modifier.height(Dimens.lg))
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            experiences.forEachIndexed { index, exp ->
                Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(24.dp).fillMaxHeight()
                    ) {
                        // Line segment above the dot
                        if (index > 0) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(8.dp)
                                    .background(BorderColor.copy(alpha = 0.5f))
                            )
                        } else {
                            Spacer(Modifier.height(8.dp))
                        }

                        // Dot
                        val isActive = index == 0
                        if (isActive) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(GitBlue)
                                    .border(2.dp, GitBlue.copy(alpha = 0.5f), CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), CircleShape)
                                    .background(Color.Transparent)
                            )
                        }

                        // Line segment below the dot
                        if (index < experiences.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .weight(1f)
                                    .background(BorderColor.copy(alpha = 0.5f))
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.width(Dimens.md))
                    Column(Modifier.weight(1f).padding(bottom = Dimens.xl)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text(
                                text = exp.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = exp.period,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = JetBrainsMono
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = GitBlue, fontWeight = FontWeight.Bold)) { append(exp.company) }
                                if (exp.role.isNotEmpty()) {
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) { append("  •  ${exp.role}") }
                                }
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = JetBrainsMono,
                        )
                        Spacer(Modifier.height(Dimens.sm))
                        Text(
                            text = exp.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                        if (exp.highlights.isNotEmpty()) {
                            Spacer(Modifier.height(Dimens.md))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                    .padding(Dimens.md),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = stringResource(Res.string.impact_highlights),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = JetBrainsMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                exp.highlights.forEach { highlight ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(GitGreen)
                                        )
                                        Text(
                                            text = highlight,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
