package com.one.dev.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.presentation.ui.theme.M3SurfaceContainer

@Composable
fun MarkdownRenderer(
    markdownText: String,
    modifier: Modifier = Modifier
) {
    val lines = markdownText.split("\n")
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var inCodeBlock = false
        val codeBlockLines = mutableListOf<String>()

        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("```")) {
                if (inCodeBlock) {
                    // End of code block
                    CodeBlock(codeBlockLines.joinToString("\n"))
                    codeBlockLines.clear()
                    inCodeBlock = false
                } else {
                    inCodeBlock = true
                }
                continue
            }

            if (inCodeBlock) {
                codeBlockLines.add(line)
                continue
            }

            if (trimmedLine.startsWith("# ")) {
                Text(
                    text = parseMarkdownLine(trimmedLine.drop(2)),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            } else if (trimmedLine.startsWith("## ")) {
                Text(
                    text = parseMarkdownLine(trimmedLine.drop(3)),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            } else if (trimmedLine.startsWith("### ")) {
                Text(
                    text = parseMarkdownLine(trimmedLine.drop(4)),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            } else if (trimmedLine.startsWith("- ") || trimmedLine.startsWith("* ")) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = parseMarkdownLine(trimmedLine.drop(2)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (trimmedLine.isNotEmpty()) {
                Text(
                    text = parseMarkdownLine(line),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            } else {
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

private fun parseMarkdownLine(line: String): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        while (cursor < line.length) {
            val boldStart = line.indexOf("**", cursor)
            val codeStart = line.indexOf("`", cursor)
            
            val firstFormat = when {
                boldStart != -1 && codeStart != -1 -> if (boldStart < codeStart) "bold" else "code"
                boldStart != -1 -> "bold"
                codeStart != -1 -> "code"
                else -> null
            }
            
            if (firstFormat == null) {
                append(line.substring(cursor))
                break
            }
            
            when (firstFormat) {
                "bold" -> {
                    append(line.substring(cursor, boldStart))
                    val boldEnd = line.indexOf("**", boldStart + 2)
                    if (boldEnd != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(line.substring(boldStart + 2, boldEnd))
                        }
                        cursor = boldEnd + 2
                    } else {
                        append("**")
                        cursor = boldStart + 2
                    }
                }
                "code" -> {
                    append(line.substring(cursor, codeStart))
                    val codeEnd = line.indexOf("`", codeStart + 1)
                    if (codeEnd != -1) {
                        withStyle(SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF5CE2F5),
                            background = Color.White.copy(alpha = 0.05f)
                        )) {
                            append(line.substring(codeStart + 1, codeEnd))
                        }
                        cursor = codeEnd + 1
                    } else {
                        append("`")
                        cursor = codeStart + 1
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(M3SurfaceContainer)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
