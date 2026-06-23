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
import com.one.dev.data.models.PortfolioProfile
import com.one.dev.navigation.Routes
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
                            uriHandler.openUri(profile.github)
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

    val textColor = if (isActive) neutralBg else primaryColor
    val backgroundColor = if (isActive) primaryColor else Color.Transparent
    val borderStroke = if (isActive) null else BorderStroke(1.dp, primaryColor.copy(alpha = 0.8f))

    // Futuristic cut corner shape for a cyberpunk tech feel
    val tabShape = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp)

    Box(
        modifier = modifier
            .clip(tabShape)
            .background(backgroundColor)
            .then(if (borderStroke != null) Modifier.border(borderStroke, tabShape) else Modifier)
            .clickable { onClick() }
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Left
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(GitGreen))
                Text(
                    "Branch: main*",
                    style = MaterialTheme.typography.labelSmall,
                    color = GitGreen,
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp
                )
            }
        }

        // Right
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(GitGreen))
                Text(
                    "ONLINE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GitGreen,
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp
                )
            }
        }
    }
}

val GithubIcon: ImageVector
    get() = ImageVector.Builder(
        name = "GithubIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(12.0f, 2.0f)
        curveTo(6.477f, 2.0f, 2.0f, 6.484f, 2.0f, 12.017f)
        curveTo(2.0f, 16.446f, 4.87f, 20.199f, 8.852f, 21.528f)
        curveTo(9.352f, 21.622f, 9.534f, 21.312f, 9.534f, 21.046f)
        curveTo(9.534f, 20.812f, 9.525f, 20.192f, 9.52f, 19.367f)
        curveTo(6.738f, 19.972f, 6.15f, 18.03f, 6.15f, 18.03f)
        curveTo(5.696f, 16.877f, 5.038f, 16.57f, 5.038f, 16.57f)
        curveTo(4.13f, 15.946f, 5.107f, 15.958f, 5.107f, 15.958f)
        curveTo(6.111f, 16.029f, 6.64f, 16.992f, 6.64f, 16.992f)
        curveTo(7.533f, 18.527f, 8.984f, 18.082f, 9.554f, 17.828f)
        curveTo(9.645f, 17.178f, 9.905f, 16.737f, 10.192f, 16.485f)
        curveTo(7.971f, 16.231f, 5.636f, 15.369f, 5.636f, 11.517f)
        curveTo(5.636f, 10.42f, 6.026f, 9.522f, 6.663f, 8.82f)
        curveTo(6.56f, 8.565f, 6.218f, 7.544f, 6.762f, 6.162f)
        curveTo(6.762f, 6.162f, 7.604f, 5.891f, 9.516f, 7.19f)
        curveTo(10.317f, 6.966f, 11.173f, 6.854f, 12.022f, 6.85f)
        curveTo(12.87f, 6.854f, 13.727f, 6.966f, 14.53f, 7.19f)
        curveTo(16.44f, 5.891f, 17.28f, 6.162f, 17.28f, 6.162f)
        curveTo(17.826f, 7.544f, 17.483f, 8.565f, 17.382f, 8.82f)
        curveTo(18.021f, 9.522f, 18.407f, 10.42f, 18.407f, 11.517f)
        curveTo(18.407f, 15.378f, 16.07f, 16.228f, 13.842f, 16.477f)
        curveTo(14.2f, 16.786f, 14.518f, 17.399f, 14.518f, 18.336f)
        curveTo(14.518f, 19.679f, 14.507f, 20.763f, 14.507f, 21.046f)
        curveTo(14.507f, 21.315f, 14.685f, 21.628f, 15.195f, 21.528f)
        curveTo(19.173f, 20.194f, 22.0f, 16.443f, 22.0f, 12.017f)
        curveTo(22.0f, 6.484f, 17.522f, 2.0f, 12.0f, 2.0f)
        close()
    }.build()
