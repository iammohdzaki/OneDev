package com.one.dev.data.models

import kotlinx.serialization.Serializable

data class ProjectType(
    val id: String,
    val displayName: String
)

data class ProjectGroup(
    val displayName: String,
    val emoji: String
)

enum class ProjectStatus { ACTIVE, MAINTAINED, ARCHIVED }

data class PortfolioProject(
    val id: String,
    val title: String,
    val description: String,
    val group: ProjectGroup,
    val type: ProjectType,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val techStack: List<String> = emptyList(),
    val githubUrl: String = "",
    val demoUrl: String? = null,
    val stars: Int = 0,
    val featured: Boolean = false,
    val currentWork: String? = null,
    val details: String = "",
    val isPublic: Boolean = false,
    val buttonText: String? = null
)

data class PortfolioProfile(
    val name: String = "",
    val tagline: String = "",
    val bio: String = "",
    val skills: List<String> = emptyList(),
    val stats: ProfileStats = ProfileStats(),
    val repoUrl: String = "",
    val repoLabel: String = "",
    val socials: List<SocialLink> = emptyList(),
    val skillBars: List<SkillBar> = emptyList(),
    val experiences: List<Experience> = emptyList(),
    val philosophy: List<CorePhilosophy> = emptyList(),
    val cvUrl: String = "",
    val discordUrl: String = ""
) {
    val github: String
        get() = socials.firstOrNull { it.label.equals("Github", ignoreCase = true) }?.url.orEmpty()
    val linkedin: String
        get() = socials.firstOrNull { it.label.equals("LinkedIn", ignoreCase = true) }?.url.orEmpty()
    val email: String
        get() = socials.firstOrNull { it.label.equals("Email", ignoreCase = true) }?.url.orEmpty()

    companion object {
        val EMPTY = PortfolioProfile()
    }
}

data class ProfileStats(
    val yearsExperience: Int = 0,
    val projectsBuilt: Int = 0,
    val articlesWritten: Int = 0,
    val commitsThisYear: String = "0",
)

// ── Serializable DTOs ─────────────────────────────────────────────────────────

@Serializable
data class ProfileDto(
    val name: String,
    val tagline: String,
    val bio: String,
    val skills: List<String>,
    val stats: ProfileStatsDto,
    val repoUrl: String = "",
    val repoLabel: String = "",
    val socials: List<SocialLink> = emptyList(),
    val skillBars: List<SkillBar> = emptyList(),
    val experiences: List<Experience> = emptyList(),
    val philosophy: List<CorePhilosophy> = emptyList(),
    val cvUrl: String = "",
    val discordUrl: String = ""
)

@Serializable
data class ProfileStatsDto(
    val yearsExperience: Int,
    val projectsBuilt: Int,
    val articlesWritten: Int,
    val commitsThisYear: String
)

@Serializable
data class ProjectDto(
    val id: String,
    val title: String,
    val description: String,
    val releaseStatus: Int,
    val tags: List<String>,
    val type: String,
    val projectUrl: String,
    val githubUrl: String = "",
    val details: String = "",
    val group: String,
    val isPinned: Boolean = false,
    val stars: Int = 0,
    val groupEmoji: String? = null,
    val category: String = "Project",
    val currentWork: String? = null,
    val buttonText: String? = null
)

// ── Mapper Extensions ─────────────────────────────────────────────────────────

fun ProfileDto.toDomain() = PortfolioProfile(
    name = name,
    tagline = tagline,
    bio = bio,
    skills = skills,
    stats = ProfileStats(
        yearsExperience = stats.yearsExperience,
        projectsBuilt = stats.projectsBuilt,
        articlesWritten = stats.articlesWritten,
        commitsThisYear = stats.commitsThisYear
    ),
    repoUrl = repoUrl,
    repoLabel = repoLabel,
    socials = socials,
    skillBars = skillBars,
    experiences = experiences,
    philosophy = philosophy,
    cvUrl = cvUrl,
    discordUrl = discordUrl
)

fun ProjectDto.toDomain(): PortfolioProject {
    val groupDomain = ProjectGroup(
        displayName = group,
        emoji = groupEmoji ?: "🔹"
    )

    val typeDomain = ProjectType(
        id = category.lowercase(),
        displayName = category
    )

    val statusEnum = when (releaseStatus) {
        1 -> ProjectStatus.ACTIVE
        else -> ProjectStatus.MAINTAINED
    }

    return PortfolioProject(
        id = id,
        title = title,
        description = description,
        group = groupDomain,
        type = typeDomain,
        status = statusEnum,
        techStack = tags,
        githubUrl = githubUrl,
        demoUrl = if (projectUrl.isNotEmpty()) projectUrl else null,
        stars = stars,
        featured = isPinned,
        currentWork = currentWork,
        details = details,
        isPublic = type.equals("public", ignoreCase = true),
        buttonText = buttonText
    )
}

@Serializable
data class SkillBar(
    val name: String,
    val percentage: Float
)

@Serializable
data class Experience(
    val title: String,
    val company: String,
    val role: String,
    val period: String,
    val description: String,
    val highlights: List<String> = emptyList()
)

@Serializable
data class CorePhilosophy(
    val emoji: String,
    val text: String,
    val bold: String
)

@Serializable
data class SocialLink(
    val label: String,
    val url: String
)
