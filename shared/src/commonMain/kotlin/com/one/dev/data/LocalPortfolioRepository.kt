package com.one.dev.data

import com.one.dev.data.models.*
import kotlinx.serialization.json.Json
import onedev.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

object LocalPortfolioRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private var cachedProfile: PortfolioProfile? = null
    private var cachedProjects: List<PortfolioProject>? = null

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getProfile(): PortfolioProfile {
        cachedProfile?.let { return it }
        return try {
            val bytes = Res.readBytes("files/profile.json")
            val content = decodeUtf8(bytes)
            val dto = json.decodeFromString<ProfileDto>(content)
            dto.toDomain().also { cachedProfile = it }
        } catch (e: Exception) {
            PortfolioProfile(name = "Error loading profile: ${e.message}")
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getProjects(): List<PortfolioProject> {
        cachedProjects?.let { return it }
        return try {
            val bytes = Res.readBytes("files/projects.json")
            val content = decodeUtf8(bytes)
            val dtos = json.decodeFromString<List<ProjectDto>>(content)
            dtos.map { it.toDomain() }.also { cachedProjects = it }
        } catch (e: Exception) {
            emptyList()
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getProjectDetailsMarkdown(detailsPath: String): String {
        return try {
            val bytes = Res.readBytes("files/$detailsPath")
            decodeUtf8(bytes)
        } catch (e: Exception) {
            "Error loading project details: ${e.message}"
        }
    }

    /**
     * Pure Kotlin UTF-8 decoder. Bypasses standard library 'ByteArray.decodeToString'
     * which fails on JS/WasmJs compilation when compiler cache is corrupted.
     */
    private fun decodeUtf8(bytes: ByteArray): String {
        val sb = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i].toInt() and 0xFF
            if (b < 0x80) {
                sb.append(b.toChar())
                i++
            } else if (b < 0xE0) {
                if (i + 1 >= bytes.size) break
                val b2 = bytes[i + 1].toInt() and 0xFF
                val code = ((b and 0x1F) shl 6) or (b2 and 0x3F)
                sb.append(code.toChar())
                i += 2
            } else if (b < 0xF0) {
                if (i + 2 >= bytes.size) break
                val b2 = bytes[i + 1].toInt() and 0xFF
                val b3 = bytes[i + 2].toInt() and 0xFF
                val code = ((b and 0x0F) shl 12) or ((b2 and 0x3F) shl 6) or (b3 and 0x3F)
                sb.append(code.toChar())
                i += 3
            } else {
                if (i + 3 >= bytes.size) break
                val b2 = bytes[i + 1].toInt() and 0xFF
                val b3 = bytes[i + 2].toInt() and 0xFF
                val b4 = bytes[i + 3].toInt() and 0xFF
                var code = ((b and 0x07) shl 18) or ((b2 and 0x3F) shl 12) or ((b3 and 0x3F) shl 6) or (b4 and 0x3F)
                if (code >= 0x10000) {
                    code -= 0x10000
                    sb.append((0xD800 or (code shr 10)).toChar())
                    sb.append((0xDC00 or (code and 0x3FF)).toChar())
                } else {
                    sb.append(code.toChar())
                }
                i += 4
            }
        }
        return sb.toString()
    }
}
