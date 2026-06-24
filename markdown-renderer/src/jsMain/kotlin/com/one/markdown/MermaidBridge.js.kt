package com.one.markdown

import kotlinx.coroutines.await
import kotlin.js.Promise
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image as SkiaImage

private external val window: dynamic

actual suspend fun renderMermaid(text: String, elementId: String): String {
    return try {
        val renderPromise = window.renderMermaid(text, elementId) as Promise<String>
        renderPromise.await()
    } catch (e: Throwable) {
        "Error rendering Mermaid: ${e.message}"
    }
}

actual fun showMermaidModal(svgHtml: String) {
    try {
        window.showMermaidModal(svgHtml)
    } catch (e: Throwable) {
        // Ignore
    }
}

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun fetchSvgAsPng(url: String): ByteArray? {
    return try {
        val promise = window.fetchSvgAsPngBase64(url) as Promise<String>
        val base64Str = promise.await()
        Base64.decode(base64Str)
    } catch (e: Throwable) {
        console.error("JS fetchSvgAsPng failed for $url", e)
        null
    }
}

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun fetchImageBytes(url: String): ByteArray? {
    return try {
        val promise = window.fetchImageBytesBase64(url) as Promise<String>
        val base64Str = promise.await()
        Base64.decode(base64Str)
    } catch (e: Throwable) {
        console.error("JS fetchImageBytes failed for $url", e)
        null
    }
}

actual fun decodeImageBitmap(bytes: ByteArray): ImageBitmap? {
    return try {
        SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
    } catch (e: Throwable) {
        null
    }
}
