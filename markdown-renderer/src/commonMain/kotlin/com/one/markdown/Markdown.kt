package com.one.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Markdown(
    content: String,
    modifier: Modifier = Modifier,
    basePath: String = "",
    repoUrl: String = ""
) {
    RenderMarkdown(markdownText = content, modifier = modifier, basePath = basePath, repoUrl = repoUrl)
}
