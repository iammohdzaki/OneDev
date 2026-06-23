package com.one.dev.presentation.ui.about.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalUriHandler
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
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.md)) {
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
        }
    }
}
