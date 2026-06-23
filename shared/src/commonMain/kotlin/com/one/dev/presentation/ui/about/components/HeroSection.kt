package com.one.dev.presentation.ui.about.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun HeroSection(name: String, bio: String, modifier: Modifier = Modifier) {
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
                text = "KMP / Android Engineer",
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
            text = buildAnnotatedString {
                append("Obsessed with developer experience (DX) and crafting fluid, high-performance UIs. Creator of ")
                withStyle(SpanStyle(color = GitGreen, fontWeight = FontWeight.SemiBold)) { append("OneKore") }
                append(" and ")
                withStyle(SpanStyle(color = GitGreen, fontWeight = FontWeight.SemiBold)) { append("FixLag") }
                append(". Bridging the gap between robust multiplatform architecture and pixel-perfect aesthetics.")
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 26.sp,
        )
        Spacer(Modifier.height(Dimens.lg))
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.md)) {
            Button(
                onClick = {},
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
            OutlinedButton(
                onClick = {},
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
