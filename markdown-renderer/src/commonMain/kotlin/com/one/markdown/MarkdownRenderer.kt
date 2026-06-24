package com.one.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.skia.Image as SkiaImage

enum class Align { LEFT, CENTER }
data class Segment(val text: String, val align: Align)

// ─────────────────────────────────────────────────────────────────────────────
// URL normalisation – GitHub blob → raw.githubusercontent
// ─────────────────────────────────────────────────────────────────────────────
private fun normalizeImageUrl(url: String, repoUrl: String): String {
    var normalized = url.trim()
    
    // 1. Dynamic Rename/Fork Redirections (configured from the project metadata)
    if (repoUrl.isNotEmpty() && repoUrl.contains("github.com/")) {
        val cleanRepoUrl = repoUrl.trim().removeSuffix("/").removeSuffix(".git")
        val partsRepo = cleanRepoUrl.split("github.com/")
        if (partsRepo.size >= 2) {
            val segments = partsRepo[1].split("/").filter { it.isNotEmpty() }
            if (segments.size >= 2) {
                val owner = segments[0]
                val repo = segments[1]
                
                val cleanRepo = repo.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
                val ignoredKeywords = setOf("blob", "tree", "raw", "actions", "workflow", "status", "badges", "main", "master", "github", "issues", "pulls", "releases", "tags")
                
                val urlParts = normalized.split("/")
                for (i in 0 until urlParts.size - 1) {
                    val someOwner = urlParts[i]
                    val someRepo = urlParts[i + 1]
                    val cleanSomeRepo = someRepo.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
                    if (cleanSomeRepo.length >= 3 && cleanSomeRepo !in ignoredKeywords) {
                        if (cleanRepo.startsWith(cleanSomeRepo) || cleanSomeRepo.startsWith(cleanRepo)) {
                            val targetReplace = "$someOwner/$someRepo"
                            val replacement = "$owner/$repo"
                            if (normalized.contains(targetReplace, ignoreCase = true) && 
                                !normalized.contains(replacement, ignoreCase = true)) {
                                normalized = normalized.replace(targetReplace, replacement, ignoreCase = true)
                            }
                        }
                    }
                }
            }
        }
    }
    
    // 2. Normalize GitHub URLs to raw content URLs
    if (normalized.startsWith("https://github.com/", ignoreCase = true)) {
        // Exclude workflow actions badges which should remain pointing to GitHub APIs
        if (!normalized.contains("/actions/workflow/", ignoreCase = true)) {
            if (normalized.contains("/blob/", ignoreCase = true)) {
                normalized = normalized
                    .replace("https://github.com/", "https://raw.githubusercontent.com/", ignoreCase = true)
                    .replace("/blob/", "/", ignoreCase = true)
            } else if (normalized.contains("/raw/", ignoreCase = true)) {
                normalized = normalized
                    .replace("https://github.com/", "https://raw.githubusercontent.com/", ignoreCase = true)
                    .replace("/raw/", "/", ignoreCase = true)
            } else {
                // If it contains /main/ or /master/ directly
                val mainIndex = normalized.indexOf("/main/", ignoreCase = true)
                if (mainIndex > 0) {
                    normalized = "https://raw.githubusercontent.com/" + normalized.substring("https://github.com/".length)
                } else {
                    val masterIndex = normalized.indexOf("/master/", ignoreCase = true)
                    if (masterIndex > 0) {
                        normalized = "https://raw.githubusercontent.com/" + normalized.substring("https://github.com/".length)
                    }
                }
            }
        }
    }
    return normalized
}

// ─────────────────────────────────────────────────────────────────────────────
// Pre-process raw markdown / HTML before handing it to the markdown parser.
// ─────────────────────────────────────────────────────────────────────────────
private fun preprocessMarkdown(markdownText: String, repoUrl: String): String {
    var text = markdownText

    // 1. Remove HTML comments
    text = Regex("<!--[\\s\\S]*?-->").replace(text, "")

    // 2. Handle <picture> tags: pick the <img> src inside them
    //    We extract only the inner <img> and discard <source> alternatives.
    text = Regex("<picture[^>]*>([\\s\\S]*?)</picture>", RegexOption.IGNORE_CASE).replace(text) { m ->
        val inner = m.groupValues[1]
        // Pull the <img> from inside
        val imgMatch = Regex("<img([^>]+)>", RegexOption.IGNORE_CASE).find(inner)
        if (imgMatch != null) imgMatch.value else ""
    }

    // 3. Convert HTML <img> tags to markdown image syntax
    val srcRegex = Regex("""src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
    val altRegex = Regex("""alt=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
    text = Regex("<img([^>]+)>", RegexOption.IGNORE_CASE).replace(text) { m ->
        val attrs = m.groupValues[1]
        val src   = srcRegex.find(attrs)?.groupValues?.get(1) ?: ""
        val alt   = altRegex.find(attrs)?.groupValues?.get(1) ?: "Image"
        if (src.isNotEmpty()) "![${alt}](${normalizeImageUrl(src, repoUrl)})" else ""
    }

    // 4. Convert heading tags
    text = Regex("<h1[^>]*>([\\s\\S]*?)</h1>", RegexOption.IGNORE_CASE).replace(text) { "# " + it.groupValues[1].trim() }
    text = Regex("<h2[^>]*>([\\s\\S]*?)</h2>", RegexOption.IGNORE_CASE).replace(text) { "## " + it.groupValues[1].trim() }
    text = Regex("<h3[^>]*>([\\s\\S]*?)</h3>", RegexOption.IGNORE_CASE).replace(text) { "### " + it.groupValues[1].trim() }
    text = Regex("<h4[^>]*>([\\s\\S]*?)</h4>", RegexOption.IGNORE_CASE).replace(text) { "#### " + it.groupValues[1].trim() }
    text = Regex("<h5[^>]*>([\\s\\S]*?)</h5>", RegexOption.IGNORE_CASE).replace(text) { "##### " + it.groupValues[1].trim() }
    text = Regex("<h6[^>]*>([\\s\\S]*?)</h6>", RegexOption.IGNORE_CASE).replace(text) { "###### " + it.groupValues[1].trim() }

    // 5. <kbd> → code-span with kbd prefix
    text = Regex("<kbd[^>]*>([\\s\\S]*?)</kbd>", RegexOption.IGNORE_CASE)
         .replace(text) { "`kbd:" + it.groupValues[1].trim() + "`" }

    // 6. <br> → newline
    text = Regex("<br\\s*/?>", RegexOption.IGNORE_CASE).replace(text, "\n")

    // 7. Also normalize raw GitHub blob image URLs that are already in markdown syntax
    //    e.g. ![alt](https://github.com/user/repo/blob/main/img.svg)
    text = Regex("""!\[([^\]]*)\]\(([^)]+)\)""").replace(text) { m ->
        val alt = m.groupValues[1]
        val url = normalizeImageUrl(m.groupValues[2].trim(), repoUrl)
        "![${alt}](${url})"
    }

    // 8. Strip all remaining HTML tags
    text = Regex("<[^>]+>").replace(text, "")

    return text
}

// ─────────────────────────────────────────────────────────────────────────────
// Split markdown into left-aligned and centre-aligned segments.
// Handles <div align="center">, <p align="center">, and stand-alone
// heading tags with align="center".
// ─────────────────────────────────────────────────────────────────────────────
private fun segmentMarkdown(markdown: String): List<Segment> {
    val segments = mutableListOf<Segment>()
    var remaining = markdown

    // Matches the opening of a block-level HTML element that requests centering
    val centerOpenRegex = Regex(
        "<(div|p|h[1-6])\\s[^>]*align=[\"']center[\"'][^>]*>",
        RegexOption.IGNORE_CASE
    )

    while (remaining.isNotEmpty()) {
        val match = centerOpenRegex.find(remaining) ?: run {
            segments.add(Segment(remaining, Align.LEFT))
            break
        }

        val startIdx   = match.range.first
        val tagEndIdx  = match.range.last + 1
        val tagName    = match.groupValues[1].lowercase()

        // Content before this tag → LEFT segment
        if (startIdx > 0) {
            segments.add(Segment(remaining.substring(0, startIdx), Align.LEFT))
        }

        // Find the matching closing tag (handles ONE level of nesting for div)
        val closeRegex = Regex("</$tagName>", RegexOption.IGNORE_CASE)
        val closeMatch = closeRegex.find(remaining, tagEndIdx)
        if (closeMatch == null) {
            // No closing tag found – treat rest as centred
            segments.add(Segment(remaining.substring(tagEndIdx), Align.CENTER))
            break
        }

        val closeIdx    = closeMatch.range.first
        val closeTagEnd = closeMatch.range.last + 1
        val content     = remaining.substring(tagEndIdx, closeIdx)

        // For standalone heading tags prepend the hashes so the parser handles them
        val adjustedContent = if (tagName.startsWith("h") && tagName.length == 2) {
            val level  = tagName[1].digitToIntOrNull() ?: 1
            val hashes = "#".repeat(level)
            "$hashes $content"
        } else {
            content
        }

        segments.add(Segment(adjustedContent, Align.CENTER))
        remaining = remaining.substring(closeTagEnd)
    }

    return segments
}

// ─────────────────────────────────────────────────────────────────────────────
// Top-level composable
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun RenderMarkdown(
    markdownText: String, 
    modifier: Modifier = Modifier, 
    basePath: String = "",
    repoUrl: String = ""
) {
    val segments = remember(markdownText) { segmentMarkdown(markdownText) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (segment in segments) {
            val alignment    = if (segment.align == Align.CENTER) Alignment.CenterHorizontally else Alignment.Start
            val preprocessed = remember(segment.text, repoUrl) { preprocessMarkdown(segment.text, repoUrl) }
            val flavour      = remember { GFMFlavourDescriptor() }
            val parsedTree   = remember(preprocessed) {
                MarkdownParser(flavour).buildMarkdownTreeFromString(preprocessed)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = alignment,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RenderChildren(parsedTree, preprocessed, alignment, basePath, repoUrl)
            }
        }
    }
}

@Composable
fun RenderChildren(
    node: ASTNode, 
    text: String, 
    alignment: Alignment.Horizontal, 
    basePath: String = "",
    repoUrl: String = ""
) {
    for (child in node.children) {
        RenderBlockNode(child, text, alignment, basePath, repoUrl)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Block-node renderer
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RenderBlockNode(
    node: ASTNode, 
    text: String, 
    alignment: Alignment.Horizontal, 
    basePath: String = "",
    repoUrl: String = ""
) {
    val nodeType = node.type.name
    val nodeText = text.substring(node.startOffset, node.endOffset)

    when (nodeType) {
        "MARKDOWN_FILE" -> {
            RenderChildren(node, text, alignment, basePath, repoUrl)
        }

        "PARAGRAPH" -> {
            val children = node.children

            // Classify children
            val nonWhitespace = children.filter {
                it.type.name != "WHITE_SPACE" && it.type.name != "EOL"
            }
            val imageOrLinkNodes = nonWhitespace.filter { child ->
                val t = child.type.name
                t == "IMAGE" ||
                (t == "INLINE_LINK" && child.children.any { c ->
                    c.type.name == "LINK_TEXT" && c.children.any { cc -> cc.type.name == "IMAGE" }
                })
            }

            if (imageOrLinkNodes.isNotEmpty() && imageOrLinkNodes.size >= nonWhitespace.size) {
                // Pure image paragraph → render as a FlowRow of images/badges
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = if (alignment == Alignment.CenterHorizontally)
                        Arrangement.Center else Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (imgNode in imageOrLinkNodes) {
                        val info = extractImageInfo(imgNode, text)
                        if (info.imgUrl.isNotEmpty()) {
                            MarkdownImage(
                                url   = normalizeImageUrl(info.imgUrl, repoUrl),
                                alt   = info.alt,
                                clickUrl = if (info.clickUrl.isNotEmpty()) info.clickUrl else info.imgUrl,
                                modifier = Modifier.wrapContentSize(),
                                basePath = basePath,
                                repoUrl = repoUrl
                            )
                        }
                    }
                }
            } else {
                // Mixed paragraph – inline rendering
                // Check if there are inline images among the children; render them composable
                val hasInlineImages = nonWhitespace.any { it.type.name == "IMAGE" }
                if (hasInlineImages) {
                    // Render mixed content: text + inline images
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = if (alignment == Alignment.CenterHorizontally)
                            Arrangement.Center else Arrangement.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (child in children) {
                            when (child.type.name) {
                                "IMAGE" -> {
                                    val info = extractImageInfo(child, text)
                                    if (info.imgUrl.isNotEmpty()) {
                                        MarkdownImage(
                                            url   = normalizeImageUrl(info.imgUrl, repoUrl),
                                            alt   = info.alt,
                                            modifier = Modifier.wrapContentSize(),
                                            basePath = basePath,
                                            repoUrl = repoUrl
                                        )
                                    }
                                }
                                "WHITE_SPACE", "EOL" -> {
                                    Text(" ", style = MaterialTheme.typography.bodyMedium)
                                }
                                else -> {
                                    val annotated = buildAnnotatedString { appendNode(child, text) }
                                    if (annotated.isNotEmpty()) {
                                        MarkdownClickableText(
                                            annotatedString = annotated,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = if (alignment == Alignment.CenterHorizontally)
                                                    TextAlign.Center else TextAlign.Start
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val annotated = buildInlineString(node, text)
                    MarkdownClickableText(
                        annotatedString = annotated,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = if (alignment == Alignment.CenterHorizontally)
                                TextAlign.Center else TextAlign.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        "ATX_1", "ATX_2", "ATX_3", "ATX_4", "ATX_5", "ATX_6" -> {
            val headingLevel = nodeType.last().toString().toIntOrNull() ?: 1
            val contentNode  = node.children.find {
                it.type.name.contains("CONTENT") ||
                it.type.name == "TEXT" ||
                it.type.name == "PARAGRAPH"
            }
            val annotated = if (contentNode != null) {
                buildInlineString(contentNode, text)
            } else {
                buildInlineString(node, text)
            }

            val style = when (headingLevel) {
                1    -> MaterialTheme.typography.headlineLarge.copy(
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
                2    -> MaterialTheme.typography.headlineMedium.copy(
                    color      = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
                3    -> MaterialTheme.typography.titleLarge.copy(
                    color      = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
                else -> MaterialTheme.typography.titleMedium.copy(
                    color      = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
            }

            // Divider under H1/H2
            if (headingLevel <= 2) {
                Text(
                    text  = annotated,
                    style = style,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = (16 / headingLevel).dp, bottom = 4.dp)
                )
            } else {
                Text(
                    text  = annotated,
                    style = style,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = (16 / headingLevel).dp, bottom = 4.dp)
                )
            }
        }

        "SETEXT_1", "SETEXT_2" -> {
            val headingLevel = if (nodeType == "SETEXT_1") 1 else 2
            val contentNode  = node.children.find {
                it.type.name.contains("CONTENT") || it.type.name == "TEXT"
            }
            val annotated = if (contentNode != null) buildInlineString(contentNode, text)
                            else buildInlineString(node, text)
            val style = if (headingLevel == 1) {
                MaterialTheme.typography.headlineLarge.copy(
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
            } else {
                MaterialTheme.typography.headlineMedium.copy(
                    color      = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign  = if (alignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
                )
            }
            Text(
                text  = annotated,
                style = style,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp)
            )
        }

        "UNORDERED_LIST" -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (child in node.children) {
                    if (child.type.name == "LIST_ITEM") {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text  = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            val paraNode = child.children.find { it.type.name == "PARAGRAPH" }
                            val itemAnnotated = if (paraNode != null) buildInlineString(paraNode, text)
                                                else buildInlineString(child, text)
                            MarkdownClickableText(itemAnnotated)
                        }
                    } else if (child.type.name == "UNORDERED_LIST" || child.type.name == "ORDERED_LIST") {
                        // Nested list
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            RenderBlockNode(child, text, alignment, basePath, repoUrl)
                        }
                    }
                }
            }
        }

        "ORDERED_LIST" -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                var index = 1
                for (child in node.children) {
                    if (child.type.name == "LIST_ITEM") {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text  = "$index.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            val paraNode = child.children.find { it.type.name == "PARAGRAPH" }
                            val itemAnnotated = if (paraNode != null) buildInlineString(paraNode, text)
                                                else buildInlineString(child, text)
                            MarkdownClickableText(itemAnnotated)
                        }
                        index++
                    } else if (child.type.name == "UNORDERED_LIST" || child.type.name == "ORDERED_LIST") {
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            RenderBlockNode(child, text, alignment, basePath, repoUrl)
                        }
                    }
                }
            }
        }

        "BLOCK_QUOTE" -> {
            val contentNode = node.children.find { it.type.name == "PARAGRAPH" }
            val annotated = if (contentNode != null) buildInlineString(contentNode, text)
                            else buildInlineString(node, text)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .drawBehind {
                        drawLine(
                            color       = Color(0xFFBC8CFF),
                            start       = Offset(0f, 0f),
                            end         = Offset(0f, size.height),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            ) {
                MarkdownClickableText(
                    annotatedString = annotated,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }

        "CODE_FENCE", "CODE_BLOCK" -> {
            val codeLines   = nodeText.split("\n")
            val lang        = codeLines.firstOrNull()?.trim()?.removePrefix("```")?.trim()?.lowercase() ?: ""
            val codeContent = if (codeLines.size > 2) {
                codeLines.subList(1, codeLines.size - 1).joinToString("\n")
            } else if (codeLines.size > 1) {
                codeLines.subList(1, codeLines.size).joinToString("\n")
            } else {
                nodeText
            }

            if (lang == "mermaid") {
                MermaidDiagram(codeContent)
            } else {
                MarkdownCodeBlock(code = codeContent, language = lang)
            }
        }

        "IMAGE" -> {
            val info = extractImageInfo(node, text)
            if (info.imgUrl.isNotEmpty()) {
                MarkdownImage(
                    url = normalizeImageUrl(info.imgUrl, repoUrl),
                    alt = info.alt,
                    clickUrl = info.clickUrl,
                    basePath = basePath,
                    repoUrl = repoUrl
                )
            }
        }

        "TABLE" -> {
            MarkdownTable(node, text)
        }

        "HORIZONTAL_RULE" -> {
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            )
            Spacer(Modifier.height(8.dp))
        }

        else -> {
            if (node.children.isNotEmpty()) {
                RenderChildren(node, text, alignment, basePath, repoUrl)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Inline helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MarkdownClickableText(
    annotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color      = MaterialTheme.colorScheme.onSurfaceVariant
    )
) {
    Text(
        text     = annotatedString,
        modifier = modifier,
        style    = style
    )
}

@Composable
fun MarkdownCodeBlock(code: String, language: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text  = code,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                color      = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun MermaidDiagram(code: String) {
    var svgText by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    val uniqueId = remember { "mermaid_" + code.hashCode().toString().replace("-", "n") }

    LaunchedEffect(code) {
        try {
            svgText = renderMermaid(code, uniqueId)
        } catch (e: Exception) {
            // swallow
        } finally {
            loading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .clickable(enabled = svgText != null) { svgText?.let { showMermaidModal(it) } }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = if (loading) "Generating diagram..." else "View Architecture Diagram (Click to Expand)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color           = if (loading) MaterialTheme.colorScheme.onSurfaceVariant
                                      else MaterialTheme.colorScheme.primary,
                    textDecoration  = if (loading) TextDecoration.None else TextDecoration.Underline,
                    fontWeight      = FontWeight.Medium
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Image loading
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MarkdownImage(
    url      : String,
    alt      : String,
    clickUrl : String = url,
    modifier : Modifier = Modifier,
    basePath : String = "",
    repoUrl  : String = ""
) {
    val uriHandler = LocalUriHandler.current

    val imageState = produceState<ImageBitmap?>(initialValue = null, key1 = url, key2 = repoUrl) {
        val resolvedUrl = if (url.startsWith("http://", ignoreCase = true) ||
                             url.startsWith("https://", ignoreCase = true) ||
                             url.startsWith("data:", ignoreCase = true) ||
                             basePath.isEmpty()) {
            url
        } else {
            val base = if (basePath.endsWith("/")) basePath else "$basePath/"
            val relative = if (url.startsWith("/")) url.substring(1) else url
            base + relative
        }

        val normalized = normalizeImageUrl(resolvedUrl, repoUrl)
        val isSvg = normalized.contains(".svg", ignoreCase = true) ||
                    normalized.contains("badge", ignoreCase = true) ||
                    normalized.contains("shields.io", ignoreCase = true) ||
                    normalized.contains("github.com/", ignoreCase = true) && normalized.contains("workflows", ignoreCase = true)

        if (isSvg) {
            try {
                val bytes = fetchSvgAsPng(normalized)
                if (bytes != null && bytes.isNotEmpty()) {
                    value = SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
                }
            } catch (e: Exception) {
                // swallow
            }
        } else {
            try {
                val bytes = fetchImageBytes(normalized)
                if (bytes != null && bytes.isNotEmpty()) {
                    value = SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
                }
            } catch (e: Exception) {
                // Try as SVG fallback
                try {
                    val bytes = fetchSvgAsPng(normalized)
                    if (bytes != null && bytes.isNotEmpty()) {
                        value = SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
                    }
                } catch (ex: Exception) {
                    // swallow
                }
            }
        }
    }

    val isBadge = remember(url) {
        url.contains("badge", ignoreCase = true) ||
        url.contains("shields.io", ignoreCase = true) ||
        url.contains("badge.svg", ignoreCase = true) ||
        url.contains("img.shields", ignoreCase = true) ||
        url.contains("workflows", ignoreCase = true) ||
        url.contains("github.com", ignoreCase = true) && url.contains(".svg", ignoreCase = true)
    }

    val isLogo = !isBadge && (
        url.contains("logo", ignoreCase = true) ||
        alt.contains("logo", ignoreCase = true) ||
        url.contains("icon", ignoreCase = true) ||
        alt.contains("icon", ignoreCase = true)
    )

    val imageModifier = when {
        isBadge -> modifier
            .height(20.dp)
            .clickable { try { uriHandler.openUri(clickUrl) } catch (_: Exception) {} }
        isLogo  -> modifier
            .heightIn(max = 96.dp)
            .widthIn(max = 96.dp)
            .clickable { try { uriHandler.openUri(clickUrl) } catch (_: Exception) {} }
        else    -> modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { try { uriHandler.openUri(clickUrl) } catch (_: Exception) {} }
    }

    val bitmap = imageState.value
    if (bitmap != null) {
        Image(
            bitmap             = bitmap,
            contentDescription = alt,
            modifier           = imageModifier
        )
    } else {
        // Placeholder / link fallback
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .clickable { try { uriHandler.openUri(if (clickUrl.isNotEmpty()) clickUrl else url) } catch (_: Exception) {} }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = alt.ifEmpty { "Image" },
                style = MaterialTheme.typography.bodySmall.copy(
                    color          = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight     = FontWeight.Medium
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Table
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MarkdownTable(node: ASTNode, text: String) {
    val rows = node.children.filter { it.type.name == "TABLE_HEADER" || it.type.name == "TABLE_ROW" }
    if (rows.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
    ) {
        for ((index, row) in rows.withIndex()) {
            val cells = row.children.filter { it.type.name == "TABLE_CELL" }

            if (index > 0) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.1f)))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (row.type.name == "TABLE_HEADER") Color.White.copy(alpha = 0.05f) else Color.Transparent)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (cell in cells) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        MarkdownClickableText(
                            annotatedString = buildInlineString(cell, text),
                            style = if (row.type.name == "TABLE_HEADER") {
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color      = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Image-info extraction helper
// ─────────────────────────────────────────────────────────────────────────────

data class ImageInfo(val imgUrl: String, val clickUrl: String, val alt: String)

private fun extractImageInfo(node: ASTNode, text: String): ImageInfo {
    val nodeType = node.type.name

    fun parseImageNode(imgNode: ASTNode): ImageInfo? {
        val inlineLink = imgNode.children.find { it.type.name == "INLINE_LINK" }
        val targetNode = inlineLink ?: imgNode
        val linkTextNode = targetNode.children.find { it.type.name == "LINK_TEXT" }
        val linkDestNode = targetNode.children.find { it.type.name == "LINK_DESTINATION" }
        val alt = linkTextNode
            ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("[").removeSuffix("]") }
            ?: "Image"
        val url = linkDestNode
            ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("(").removeSuffix(")") }
            ?: ""
        return if (url.isNotEmpty()) ImageInfo(imgUrl = url, clickUrl = url, alt = alt) else null
    }
    
    when (nodeType) {
        "IMAGE" -> {
            val info = parseImageNode(node)
            if (info != null) return info
        }
        "INLINE_LINK" -> {
            val linkTextNode = node.children.find { it.type.name == "LINK_TEXT" }
            val linkDestNode = node.children.find { it.type.name == "LINK_DESTINATION" }
            val clickUrl     = linkDestNode
                ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("(").removeSuffix(")") }
                ?: ""
            val imgNode = linkTextNode?.children?.find { it.type.name == "IMAGE" }
            if (imgNode != null) {
                val imgInfo = parseImageNode(imgNode)
                if (imgInfo != null) {
                    return ImageInfo(imgUrl = imgInfo.imgUrl, clickUrl = clickUrl, alt = imgInfo.alt)
                }
            }
        }
    }
    return ImageInfo("", "", "")
}

// ─────────────────────────────────────────────────────────────────────────────
// Inline AnnotatedString builder
// ─────────────────────────────────────────────────────────────────────────────

fun buildInlineString(node: ASTNode, text: String): AnnotatedString {
    return buildAnnotatedString { appendNode(node, text) }
}

fun AnnotatedString.Builder.appendNode(node: ASTNode, text: String) {
    val nodeType = node.type.name
    val nodeText = text.substring(node.startOffset, node.endOffset)

    when (nodeType) {
        "TEXT", "WHITE_SPACE" -> append(nodeText)

        "EOL" -> append(" ")

        "EMPH" -> {
            withStyle(SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                for (child in node.children) appendNode(child, text)
            }
        }

        "STRONG" -> {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                for (child in node.children) appendNode(child, text)
            }
        }

        "STRIKETHROUGH" -> {
            withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                for (child in node.children) appendNode(child, text)
            }
        }

        "CODE_SPAN" -> {
            val rawContent = nodeText.removePrefix("`").removeSuffix("`").trim()
            val isKbd      = rawContent.startsWith("kbd:")
            val content    = if (isKbd) rawContent.removePrefix("kbd:") else rawContent

            if (isKbd) {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        color      = Color(0xFFF0F6FC),
                        background = Color(0xFF21262D),
                        fontWeight = FontWeight.Bold
                    )
                ) { append(" $content ") }
            } else {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        color      = Color(0xFFE6EDF3),
                        background = Color(0xFF161B22)
                    )
                ) { append(" $content ") }
            }
        }

        "INLINE_LINK" -> {
            val linkTextNode = node.children.find { it.type.name == "LINK_TEXT" }
            val linkDestNode = node.children.find { it.type.name == "LINK_DESTINATION" }

            // If the link text contains an IMAGE node, skip inline rendering
            // (this is handled at the block level as an image)
            val hasImage = linkTextNode?.children?.any { it.type.name == "IMAGE" } == true
            if (hasImage) return

            val label = linkTextNode
                ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("[").removeSuffix("]") }
                ?: "Link"
            val url = linkDestNode
                ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("(").removeSuffix(")") }
                ?: ""

            val start = this.length
            append(label)
            val end = this.length

            addLink(
                url   = androidx.compose.ui.text.LinkAnnotation.Url(
                    url    = url,
                    styles = androidx.compose.ui.text.TextLinkStyles(
                        style = SpanStyle(
                            color          = Color(0xFF58A6FF),
                            textDecoration = TextDecoration.Underline,
                            fontWeight     = FontWeight.Medium
                        ),
                        hoveredStyle = SpanStyle(
                            color          = Color(0xFF58A6FF),
                            textDecoration = TextDecoration.Underline,
                            background     = Color(0xFF58A6FF).copy(alpha = 0.12f)
                        )
                    )
                ),
                start = start,
                end   = end
            )
        }

        "IMAGE" -> {
            // In inline context render as a styled link-like text
            // (The block-level handler deals with actual image composables)
            val linkTextNode = node.children.find { it.type.name == "LINK_TEXT" }
            val linkDestNode = node.children.find { it.type.name == "LINK_DESTINATION" }

            val alt = linkTextNode
                ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("[").removeSuffix("]") }
                ?: "Image"
            val url = linkDestNode
                ?.let { text.substring(it.startOffset, it.endOffset).removePrefix("(").removeSuffix(")") }
                ?: ""

            if (url.isNotEmpty()) {
                val start = this.length
                append("[img] $alt")
                val end = this.length
                addLink(
                    url   = androidx.compose.ui.text.LinkAnnotation.Url(
                        url    = url,
                        styles = androidx.compose.ui.text.TextLinkStyles(
                            style = SpanStyle(
                                color          = Color(0xFFBC8CFF),
                                textDecoration = TextDecoration.Underline,
                                fontWeight     = FontWeight.SemiBold
                            ),
                            hoveredStyle = SpanStyle(
                                color          = Color(0xFFBC8CFF),
                                textDecoration = TextDecoration.Underline,
                                background     = Color(0xFFBC8CFF).copy(alpha = 0.12f)
                            )
                        )
                    ),
                    start = start,
                    end   = end
                )
            }
        }

        else -> {
            if (node.children.isEmpty()) {
                // Filter out markdown syntax punctuation
                if (nodeType !in setOf(
                    "ASTERISK", "UNDERSCORE", "BACKTICK", "EXCLAMATION_MARK",
                    "LBRACKET", "RBRACKET", "LPAREN", "RPAREN", "TILDE"
                )) {
                    append(nodeText)
                }
            } else {
                for (child in node.children) appendNode(child, text)
            }
        }
    }
}
