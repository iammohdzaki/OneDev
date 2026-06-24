package com.one.dev.presentation.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Commit
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.data.LocalPortfolioRepository
import com.one.dev.data.models.PortfolioProject
import com.one.dev.data.models.ProjectStatus
import com.one.dev.presentation.ui.components.MarkdownRenderer
import com.one.dev.presentation.ui.components.TechBadge
import com.one.dev.presentation.ui.theme.BgPrimary
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
import com.one.dev.presentation.ui.theme.M3SurfaceContainerLow
import com.one.dev.presentation.ui.theme.M3Tertiary
import org.jetbrains.compose.resources.stringResource
import onedev.shared.generated.resources.*

@Composable
fun ProjectDetailsScreen(
    projectId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var project by remember { mutableStateOf<PortfolioProject?>(null) }
    var markdownContent by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val basePath = remember(project) {
        val repo = project?.githubUrl ?: ""
        if (repo.isNotEmpty()) LocalPortfolioRepository.getRawGithubBaseUrl(repo) else ""
    }


    LaunchedEffect(projectId) {
        isLoading = true
        val projects = LocalPortfolioRepository.getProjects()
        val foundProject = projects.firstOrNull { it.id == projectId }
        project = foundProject
        if (foundProject != null) {
            markdownContent = LocalPortfolioRepository.getProjectDetailsMarkdown(foundProject)
        }
        isLoading = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GitBlue)
            }
        } else if (project == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(Res.string.project_not_found), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = GitBlue)) {
                    Text(stringResource(Res.string.go_back))
                }
            }
        } else {
            val p = project!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens.screenHorizontal)
                    .padding(bottom = Dimens.contentBottomPad)
            ) {
                Spacer(Modifier.height(Dimens.xl))

                // ── Back Button & Terminal Prompt ───────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onBack() }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = GitBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(Res.string.back_to_gallery),
                            style = MaterialTheme.typography.labelMedium,
                            color = GitBlue,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "$ ./view_project.sh --id ${p.id}",
                        style = MaterialTheme.typography.labelMedium,
                        color = GitGreen.copy(0.7f),
                        fontFamily = JetBrainsMono
                    )
                }

                Spacer(Modifier.height(Dimens.lg))

                // ── Responsive Layout ────────────────────────────────────────
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val isWide = maxWidth > 800.dp
                    if (isWide) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.xl)
                        ) {
                            Box(Modifier.weight(2f)) {
                                MarkdownSection(markdownContent ?: "", basePath = basePath, repoUrl = p.githubUrl)
                            }
                            Box(Modifier.weight(1f)) {
                                ProjectMetadataCard(project = p)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Dimens.xl)
                        ) {
                            ProjectMetadataCard(project = p)
                            MarkdownSection(markdownContent ?: "", basePath = basePath, repoUrl = p.githubUrl)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MarkdownSection(markdownText: String, basePath: String, repoUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(M3SurfaceContainerLow)
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        MarkdownRenderer(markdownText = markdownText, basePath = basePath, repoUrl = repoUrl)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectMetadataCard(
    project: PortfolioProject,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(M3SurfaceContainerLow)
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Title & Description
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.05f))
            )

            // Header
            Text(
                text = stringResource(Res.string.metadata_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = JetBrainsMono,
                letterSpacing = 1.5.sp
            )

            // Status Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.release_status_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                val statusColor = when (project.status) {
                    ProjectStatus.ACTIVE -> M3Tertiary
                    else -> MaterialTheme.colorScheme.secondary
                }
                
                val statusText = when (project.status) {
                    ProjectStatus.ACTIVE -> stringResource(Res.string.released_status)
                    else -> stringResource(Res.string.under_dev_status)
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(statusColor))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontFamily = JetBrainsMono
                    )
                }
            }

            // Group Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.group_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${project.group.emoji} ${project.group.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = JetBrainsMono
                )
            }

            // Type Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.category_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = project.type.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = JetBrainsMono
                )
            }

            // Tech Stack
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(Res.string.technologies_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.xs),
                    verticalArrangement = Arrangement.spacedBy(Dimens.xs)
                ) {
                    project.techStack.forEach { tag ->
                        TechBadge(text = tag)
                    }
                }
            }

            // Links Section
            val hasLinks = (project.isPublic && project.githubUrl.isNotEmpty()) || project.demoUrl != null
            if (hasLinks) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.links_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (project.isPublic && project.githubUrl.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { try { uriHandler.openUri(project.githubUrl) } catch (e: Exception) {} },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(Res.string.github_code_btn))
                        }
                    }

                    project.demoUrl?.let { demo ->
                        Button(
                            onClick = { try { uriHandler.openUri(demo) } catch (e: Exception) {} },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GitBlue)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Launch,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(project.buttonText ?: stringResource(Res.string.live_demo_btn))
                        }
                    }
                }
            }
        }
    }
}
