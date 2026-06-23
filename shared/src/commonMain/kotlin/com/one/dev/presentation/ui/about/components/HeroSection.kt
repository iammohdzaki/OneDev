package com.one.dev.presentation.ui.about.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalUriHandler
import com.one.dev.presentation.ui.shell.GithubIcon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
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
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        try {
                            uriHandler.openUri("mailto:$email")
                        } catch (e: Exception) {}
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.contact_me), fontWeight = FontWeight.Bold)
            }
            if (cvUrl.isNotEmpty()) {
                OutlinedButton(
                    onClick = {
                        try {
                            uriHandler.openUri(cvUrl)
                        } catch (e: Exception) {}
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("view_cv.sh", fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold)
                }
            }
            if (githubUrl.isNotEmpty()) {
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(githubUrl)
                        } catch (e: Exception) {}
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24292F), // GitHub dark gray color
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(GithubIcon, contentDescription = "GitHub", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("GitHub", fontWeight = FontWeight.Bold)
                }
            }
            if (linkedinUrl.isNotEmpty()) {
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(linkedinUrl)
                        } catch (e: Exception) {}
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A66C2), // LinkedIn blue color
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(LinkedInIcon, contentDescription = "LinkedIn", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("LinkedIn", fontWeight = FontWeight.Bold)
                }
            }
            if (discordUrl.isNotEmpty()) {
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(discordUrl)
                        } catch (e: Exception) {}
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5865F2), // Discord blurple color
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(DiscordIcon, contentDescription = "Discord", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Discord", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private val LinkedInIcon: ImageVector
    get() = ImageVector.Builder(
        name = "LinkedInIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(19.0f, 3.0f)
        lineTo(5.0f, 3.0f)
        curveTo(3.9f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f)
        lineTo(3.0f, 19.0f)
        curveTo(3.0f, 20.1f, 3.9f, 21.0f, 5.0f, 21.0f)
        lineTo(19.0f, 21.0f)
        curveTo(20.1f, 21.0f, 21.0f, 20.1f, 21.0f, 19.0f)
        lineTo(21.0f, 5.0f)
        curveTo(21.0f, 3.9f, 20.1f, 3.0f, 19.0f, 3.0f)
        close()
        moveTo(9.0f, 17.0f)
        lineTo(6.5f, 17.0f)
        lineTo(6.5f, 9.5f)
        lineTo(9.0f, 9.5f)
        lineTo(9.0f, 17.0f)
        close()
        moveTo(7.75f, 8.25f)
        curveTo(6.92f, 8.25f, 6.25f, 7.58f, 6.25f, 6.75f)
        curveTo(6.25f, 5.92f, 6.92f, 5.25f, 7.75f, 5.25f)
        curveTo(8.58f, 5.25f, 9.25f, 5.92f, 9.25f, 6.75f)
        curveTo(9.25f, 7.58f, 8.58f, 8.25f, 7.75f, 8.25f)
        close()
        moveTo(18.0f, 17.0f)
        lineTo(15.5f, 17.0f)
        lineTo(15.5f, 13.0f)
        curveTo(15.5f, 12.0f, 15.0f, 11.5f, 14.25f, 11.5f)
        curveTo(13.5f, 11.5f, 13.0f, 12.0f, 13.0f, 13.0f)
        lineTo(13.0f, 17.0f)
        lineTo(10.5f, 17.0f)
        lineTo(10.5f, 9.5f)
        lineTo(13.0f, 9.5f)
        lineTo(13.0f, 10.5f)
        curveTo(13.5f, 9.75f, 14.25f, 9.25f, 15.25f, 9.25f)
        curveTo(16.75f, 9.25f, 18.0f, 10.25f, 18.0f, 12.25f)
        lineTo(18.0f, 17.0f)
        close()
    }.build()

private val DiscordIcon: ImageVector
    get() = ImageVector.Builder(
        name = "DiscordIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(18.966f, 6.093f)
        curveTo(17.525f, 5.437f, 15.986f, 4.962f, 14.373f, 4.697f)
        curveTo(14.341f, 4.692f, 14.309f, 4.707f, 14.292f, 4.737f)
        curveTo(14.093f, 5.093f, 13.872f, 5.56f, 13.717f, 5.925f)
        curveTo(11.996f, 5.667f, 10.287f, 5.667f, 8.59f, 5.925f)
        curveTo(8.435f, 5.56f, 8.207f, 5.093f, 8.008f, 4.737f)
        curveTo(7.991f, 4.707f, 7.959f, 4.692f, 7.927f, 4.697f)
        curveTo(6.312f, 4.962f, 4.771f, 5.437f, 3.329f, 6.093f)
        curveTo(3.316f, 6.099f, 3.305f, 6.109f, 3.299f, 6.122f)
        curveTo(0.38f, 10.485f, -0.421f, 14.743f, 0.165f, 18.937f)
        curveTo(0.168f, 18.956f, 0.178f, 18.974f, 0.193f, 18.986f)
        curveTo(2.115f, 20.398f, 3.978f, 21.256f, 5.807f, 21.822f)
        curveTo(5.84f, 21.832f, 5.875f, 21.82f, 5.895f, 21.791f)
        curveTo(6.326f, 21.303f, 6.709f, 20.781f, 7.042f, 20.228f)
        curveTo(7.063f, 20.193f, 7.046f, 20.149f, 7.01f, 20.136f)
        curveTo(6.398f, 19.903f, 5.817f, 19.619f, 5.263f, 19.29f)
        curveTo(5.223f, 19.266f, 5.221f, 19.208f, 5.259f, 19.181f)
        curveTo(5.375f, 19.094f, 5.489f, 19.003f, 5.599f, 18.91f)
        curveTo(5.618f, 18.894f, 5.644f, 18.89f, 5.666f, 18.899f)
        curveTo(9.512f, 20.662f, 13.682f, 20.662f, 17.478f, 18.899f)
        curveTo(17.5f, 18.89f, 17.526f, 18.894f, 17.545f, 18.91f)
        curveTo(17.655f, 19.003f, 17.769f, 19.094f, 17.885f, 19.181f)
        curveTo(17.923f, 19.208f, 17.921f, 19.266f, 17.881f, 19.29f)
        curveTo(17.327f, 19.619f, 16.746f, 19.903f, 16.134f, 20.136f)
        curveTo(16.098f, 20.149f, 16.081f, 20.193f, 16.102f, 20.228f)
        curveTo(16.444f, 20.781f, 16.827f, 21.303f, 17.249f, 21.791f)
        curveTo(17.269f, 21.82f, 17.304f, 21.832f, 17.337f, 21.822f)
        curveTo(19.175f, 21.256f, 21.038f, 20.398f, 22.96f, 18.986f)
        curveTo(22.975f, 18.974f, 22.985f, 18.956f, 22.988f, 18.937f)
        curveTo(23.642f, 13.974f, 21.884f, 9.77f, 18.995f, 6.122f)
        curveTo(18.99f, 6.109f, 18.979f, 6.099f, 18.966f, 6.093f)
        close()
        moveTo(8.006f, 15.932f)
        curveTo(6.9f, 15.932f, 5.992f, 14.912f, 5.992f, 13.666f)
        curveTo(5.992f, 12.42f, 6.88f, 11.4f, 8.006f, 11.4f)
        curveTo(9.141f, 11.4f, 10.039f, 12.42f, 10.019f, 13.666f)
        curveTo(10.019f, 14.912f, 9.131f, 15.932f, 8.006f, 15.932f)
        close()
        moveTo(16.012f, 15.932f)
        curveTo(14.906f, 15.932f, 13.998f, 14.912f, 13.998f, 13.666f)
        curveTo(13.998f, 12.42f, 14.886f, 11.4f, 16.012f, 11.4f)
        curveTo(17.147f, 11.4f, 18.045f, 12.42f, 18.025f, 13.666f)
        curveTo(18.025f, 14.912f, 17.137f, 15.932f, 16.012f, 15.932f)
        close()
    }.build()
