@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.one.markdown

import kotlinx.coroutines.await
import kotlin.js.Promise
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image as SkiaImage

@JsFun("(text, elementId) => window.renderMermaid(text, elementId)")
private external fun renderMermaidJs(text: JsString, elementId: JsString): Promise<JsString>

@JsFun("(svgHtml) => window.showMermaidModal(svgHtml)")
private external fun showMermaidModalJs(svgHtml: JsString)

@JsFun("(url) => window.fetchSvgAsPngBase64(url)")
private external fun fetchSvgAsPngJs(url: JsString): Promise<JsString>

@JsFun("(url) => window.fetchImageBytesBase64(url)")
private external fun fetchImageBytesJs(url: JsString): Promise<JsString>

actual suspend fun renderMermaid(text: String, elementId: String): String {
    return try {
        val promise = renderMermaidJs(text.toJsString(), elementId.toJsString())
        val result: JsString = promise.await()
        result.toString()
    } catch (e: Throwable) {
        "Error rendering Mermaid: ${e.message}"
    }
}

actual fun showMermaidModal(svgHtml: String) {
    showMermaidModalJs(svgHtml.toJsString())
}

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun fetchSvgAsPng(url: String): ByteArray? {
    return try {
        val promise = fetchSvgAsPngJs(url.toJsString())
        val base64JsStr = promise.await<JsString>()
        Base64.decode(base64JsStr.toString())
    } catch (e: Throwable) {
        println("Wasm fetchSvgAsPng failed for $url: ${e.message}")
        null
    }
}

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun fetchImageBytes(url: String): ByteArray? {
    return try {
        val promise = fetchImageBytesJs(url.toJsString())
        val base64JsStr = promise.await<JsString>()
        Base64.decode(base64JsStr.toString())
    } catch (e: Throwable) {
        println("Wasm fetchImageBytes failed for $url: ${e.message}")
        null
    }
}

actual fun decodeImageBitmap(bytes: ByteArray): ImageBitmap? {
    return try {
        SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
    } catch (e: Throwable) {
        println("Wasm decodeImageBitmap failed: ${e.message}")
        null
    }
}
