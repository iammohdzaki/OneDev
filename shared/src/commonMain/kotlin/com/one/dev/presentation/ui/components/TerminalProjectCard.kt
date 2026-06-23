package com.one.dev.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.data.models.PortfolioProject
import com.one.dev.data.models.ProjectType
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.JetBrainsMono

/**
 * Compact, bento-style project card:
 * - Left side: Cyberpunk icon placeholder matching the project category
 * - Right side: Title, type, description, stars, links, and tags
 * - No commit message block (removed for a cleaner, compact look)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TerminalProjectCard(
    project    : PortfolioProject,
    modifier   : Modifier = Modifier,
    onOpenRepo : () -> Unit = {},
    onOpenDemo : (() -> Unit)? = null,
    onClick    : () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Hover animation triggers
    val translationY by animateFloatAsState(targetValue = if (isHovered) -2f else 0f, animationSpec = tween(150), label = "translateY")
    val scale by animateFloatAsState(targetValue = if (isHovered) 1.01f else 1.0f, animationSpec = tween(150), label = "scale")

    val containerBg = if (isHovered) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isHovered) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else BorderColor.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.translationY = translationY * density
            }
            .clip(RoundedCornerShape(8.dp))
            .background(containerBg)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .hoverable(interactionSource)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left: Project Category Icon
            ProjectIcon(project.type)

            // Right: Content
            Column(modifier = Modifier.weight(1f)) {
                // Header (Title + Badges + Links)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (project.featured) {
                            Text(
                                text = "FEATURED",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 5.dp, vertical = 1.dp),
                                letterSpacing = 0.5.sp,
                            )
                        }
                    }

                    // Stars + Repo/Demo Links
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (project.stars > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${project.stars}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = JetBrainsMono
                                )
                            }
                        }
                        if (project.demoUrl != null && onOpenDemo != null) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Open Demo",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onOpenDemo() }
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                            contentDescription = "Open Repository",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onOpenRepo() }
                        )
                    }
                }

                // Subtitle (e.g. Standalone  •  Web)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${project.group.displayName}  •  ${project.type.displayName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontFamily = JetBrainsMono
                )

                // Description
                Spacer(Modifier.height(8.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                // Tech tags
                Spacer(Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    project.techStack.forEach { tag ->
                        Text(
                            text = "#$tag",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = JetBrainsMono,
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                .border(1.dp, BorderColor.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectIcon(
    type: ProjectType,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val icon = when (type.id.lowercase()) {
        "android" -> Icons.Rounded.PhoneAndroid
        "kmp", "multiplatform" -> Icons.Rounded.Devices
        "library" -> Icons.Rounded.Extension
        "web", "website" -> Icons.Rounded.Language
        "tool", "cli" -> Icons.Rounded.Build
        else -> Icons.Rounded.Code
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .border(1.dp, primaryColor.copy(alpha = 0.6f), CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = primaryColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
