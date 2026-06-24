package com.one.markdown

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun renderMermaid(text: String, elementId: String): String
expect fun showMermaidModal(svgHtml: String)
expect suspend fun fetchSvgAsPng(url: String): ByteArray?
expect suspend fun fetchImageBytes(url: String): ByteArray?
expect fun decodeImageBitmap(bytes: ByteArray): ImageBitmap?
