package com.one.dev.presentation.ui.shell

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import com.one.dev.data.models.PortfolioProfile
import com.one.dev.navigation.Routes
import com.one.dev.presentation.ui.components.GithubIcon
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
import onedev.shared.generated.resources.Res
import onedev.shared.generated.resources.tab_me
import onedev.shared.generated.resources.tab_projects
import org.jetbrains.compose.resources.stringResource

/**
 * Top bar matching Stitch header:
 * Left: Navigation tabs (Me, Projects, Blogs) - outlined by default, filled when active, with sci-fi indices
 * Right: Clean spacer / brand line
 */
@Composable
fun TopBar(
    currentRoute: Any,
    onNavigate: (Any) -> Unit,
    profile: PortfolioProfile,
    pathLabel: String = "root@devterm ~ %",
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp) // slightly taller for a premium header height
            .background(MaterialTheme.colorScheme.surface)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Left: navigation tabs (moved here, path label and cat.md removed completely) ──
        Row(
            modifier = Modifier.padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val tabs = listOf(
                TabItem("01", stringResource(Res.string.tab_me), Routes.About),
                TabItem("02", stringResource(Res.string.tab_projects), Routes.Projects)
            )

            tabs.forEach { tab ->
                val isActive = isTabActive(tab.route, currentRoute)
                NavTab(
                    index = tab.index,
                    label = tab.label,
                    isActive = isActive,
                    onClick = { onNavigate(tab.route) }
                )
            }
        }

        // Right side: Github icon
        if (profile.github.isNotEmpty()) {
            val uriHandler = LocalUriHandler.current
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            Box(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isHovered) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
                    .hoverable(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        try {
                            uriHandler.openUri(profile.repoUrl)
                        } catch (e: Exception) {
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = GithubIcon,
                    contentDescription = "GitHub Profile",
                    tint = if (isHovered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Spacer(Modifier.width(24.dp))
        }
    }
}

private data class TabItem(val index: String, val label: String, val route: Any)

private fun isTabActive(tabRoute: Any, currentRoute: Any): Boolean {
    return when (tabRoute) {
        Routes.About -> currentRoute is Routes.About
        Routes.Projects -> currentRoute is Routes.Projects || currentRoute is Routes.ProjectDetails
        else -> false
    }
}

@Composable
private fun NavTab(
    index: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary // #00F0FF (Neon Cyan)
    val neutralBg = MaterialTheme.colorScheme.background // #0C0E10

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Expressive Hover animations
    val translationY by animateFloatAsState(
        targetValue = if (isHovered) -2f else 0f,
        animationSpec = tween(150),
        label = "translateY"
    )
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.04f else 1.0f,
        animationSpec = tween(150),
        label = "scale"
    )

    val textColor = if (isActive) neutralBg else primaryColor
    val backgroundColor = when {
        isActive -> primaryColor
        isHovered -> primaryColor.copy(alpha = 0.15f)
        else -> Color.Transparent
    }
    
    val borderAlpha = if (isHovered) 1.0f else 0.8f
    val borderStroke = if (isActive) null else BorderStroke(1.dp, primaryColor.copy(alpha = borderAlpha))

    // Futuristic cut corner shape for a cyberpunk tech feel
    val tabShape = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp)

    Box(
        modifier = modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.translationY = translationY * density
            }
            .clip(tabShape)
            .background(backgroundColor)
            .then(if (borderStroke != null) Modifier.border(borderStroke, tabShape) else Modifier)
            .hoverable(interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = index,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) neutralBg.copy(alpha = 0.7f) else primaryColor.copy(alpha = 0.6f)
                )
            )
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                ),
                color = textColor
            )
        }
    }
}

@Composable
fun BottomStatusBar(
    profile: PortfolioProfile,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        val width = maxWidth
        val horizontalPad = if (width < 450.dp) 16.dp else 24.dp

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPad),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (width > 650.dp) {
                    Text(
                        text = "MIT LICENSE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp
                    )
                    Box(
                        Modifier.size(4.dp).clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    )
                }

                if (width > 450.dp) {
                    val gitInteractionSource = remember { MutableInteractionSource() }
                    val isGitHovered by gitInteractionSource.collectIsHoveredAsState()
                    Text(
                        text = profile.repoLabel.ifEmpty { "github.com/mohdzaki/OneDev" },
                        style = MaterialTheme.typography.labelSmall.copy(
                            textDecoration = if (isGitHovered) androidx.compose.ui.text.style.TextDecoration.Underline else null
                        ),
                        color = if (isGitHovered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.8f
                        ),
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        maxLines = 1,
                        modifier = Modifier
                            .hoverable(gitInteractionSource)
                            .clickable(interactionSource = gitInteractionSource, indication = null) {
                                try {
                                    uriHandler.openUri(profile.repoUrl.ifEmpty { "https://github.com/mohdzaki/OneDev" })
                                } catch (e: Exception) {
                                }
                            }
                    )
                    Box(
                        Modifier.size(4.dp).clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(GitGreen))
                    Text(
                        "Branch: main*",
                        style = MaterialTheme.typography.labelSmall,
                        color = GitGreen,
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            // Right
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (width > 650.dp) {
                    val socialLinks = profile.socials.ifEmpty {
                        listOf(
                            com.one.dev.data.models.SocialLink("Github", "https://github.com/iammohdzaki"),
                            com.one.dev.data.models.SocialLink("LinkedIn", "https://linkedin.com/in/mohammad.zaki")
                        )
                    }

                    socialLinks.forEach { link ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isHovered by interactionSource.collectIsHoveredAsState()
                        Text(
                            text = link.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                textDecoration = if (isHovered) androidx.compose.ui.text.style.TextDecoration.Underline else null
                            ),
                            color = if (isHovered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                0.6f
                            ),
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .hoverable(interactionSource)
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    try {
                                        uriHandler.openUri(link.url)
                                    } catch (e: Exception) {
                                    }
                                }
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(GitGreen))
                    Text(
                        "ONLINE",
                        style = MaterialTheme.typography.labelSmall,
                        color = GitGreen,
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
