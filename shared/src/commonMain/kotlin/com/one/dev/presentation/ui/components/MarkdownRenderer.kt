package com.one.dev.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.one.markdown.Markdown

@Composable
fun MarkdownRenderer(
    markdownText: String,
    modifier: Modifier = Modifier,
    basePath: String = "",
    repoUrl: String = ""
) {
    Markdown(content = markdownText, modifier = modifier, basePath = basePath, repoUrl = repoUrl)
}
