package com.one.dev.presentation.ui.about.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import com.one.dev.presentation.ui.components.GithubIcon
import com.one.dev.presentation.ui.components.LinkedInIcon
import com.one.dev.presentation.ui.components.DiscordIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.CompositionLocalProvider
import org.jetbrains.compose.resources.stringResource
import onedev.shared.generated.resources.*

@Composable
fun HeroSection(
    name: String,
    tagline: String,
    bio: String,
    email: String,
    cvUrl: String,
    discordUrl: String,
    githubUrl: String,
    linkedinUrl: String,
    modifier: Modifier = Modifier
) {
    val cursorTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by cursorTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    var typedText by remember(tagline) { mutableStateOf("") }
    LaunchedEffect(tagline) {
        typedText = ""
        for (i in 1..tagline.length) {
            typedText = tagline.substring(0, i)
            delay(40L)
        }
    }

    Column(modifier) {
        Text(
            text = "~.about.md",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = JetBrainsMono,
                color = GitGreen,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Dimens.xs))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = typedText,
                style = MaterialTheme.typography.headlineSmall,
                color = GitBlue,
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "|",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Bold
                ),
                color = GitBlue.copy(alpha = cursorAlpha)
            )
        }
        Spacer(Modifier.height(Dimens.md))
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 26.sp,
        )
        val uriHandler = LocalUriHandler.current
        Spacer(Modifier.height(Dimens.lg))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.md),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            ExpressiveButton(
                onClick = {
                    if (email.isNotEmpty()) {
                        try {
                            uriHandler.openUri("mailto:$email")
                        } catch (e: Exception) {}
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.contact_me), fontWeight = FontWeight.Bold)
            }
            if (cvUrl.isNotEmpty()) {
                ExpressiveButton(
                    onClick = {
                        try {
                            uriHandler.openUri(cvUrl)
                        } catch (e: Exception) {}
                    },
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    borderColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("view_cv.sh", fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold)
                }
            }
            if (githubUrl.isNotEmpty()) {
                ExpressiveButton(
                    onClick = {
                        try {
                            uriHandler.openUri(githubUrl)
                        } catch (e: Exception) {}
                    },
                    containerColor = Color(0xFF24292F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(GithubIcon, contentDescription = "GitHub", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("GitHub", fontWeight = FontWeight.Bold)
                }
            }
            if (linkedinUrl.isNotEmpty()) {
                ExpressiveButton(
                    onClick = {
                        try {
                            uriHandler.openUri(linkedinUrl)
                        } catch (e: Exception) {}
                    },
                    containerColor = Color(0xFF0A66C2),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(LinkedInIcon, contentDescription = "LinkedIn", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("LinkedIn", fontWeight = FontWeight.Bold)
                }
            }
            if (discordUrl.isNotEmpty()) {
                ExpressiveButton(
                    onClick = {
                        try {
                            uriHandler.openUri(discordUrl)
                        } catch (e: Exception) {}
                    },
                    containerColor = Color(0xFF5865F2),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(DiscordIcon, contentDescription = "Discord", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Discord", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ExpressiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.background,
    borderColor: Color? = null,
    shape: Shape = RoundedCornerShape(4.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

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

    val currentContainerColor = if (isHovered && containerColor != Color.Transparent) {
        containerColor.copy(alpha = 0.85f)
    } else {
        containerColor
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.translationY = translationY * density
            }
            .clip(shape)
            .background(currentContainerColor)
            .let {
                if (borderColor != null) {
                    val borderClr = if (isHovered) containerColor.copy(alpha = 0.85f) else borderColor
                    it.border(1.dp, borderClr, shape)
                } else {
                    it
                }
            }
            .hoverable(interactionSource)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

