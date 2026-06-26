package com.one.dev.presentation.ui.projects

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.one.dev.presentation.ui.components.NeonVerticalScrollbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.data.models.ProjectGroup
import com.one.dev.navigation.Routes
import com.one.dev.presentation.ui.components.TerminalProjectCard
import com.one.dev.presentation.ui.theme.BgPrimary
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.GitGreen
import com.one.dev.presentation.ui.theme.JetBrainsMono
import org.jetbrains.compose.resources.stringResource
import onedev.shared.generated.resources.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onNavigateTo: (Any) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProjectsContract.Effect.OpenUrl -> {
                    try { uriHandler.openUri(effect.url) } catch (e: Exception) {}
                }
            }
        }
    }

    val groupedProjects = remember(state.filteredProjects) {
        state.filteredProjects.groupBy { it.group }
    }

    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = Dimens.screenHorizontal)
                .padding(bottom = Dimens.contentBottomPad),
        ) {
            Spacer(Modifier.height(Dimens.xl))

            // ── Header ───────────────────────────────────────────────────
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val isWide = maxWidth > 700.dp
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "~.view_projects.sh",
                                style = MaterialTheme.typography.labelMedium,
                                color = GitGreen,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(Dimens.sm))
                            Text(
                                text = "Projects Gallery",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.height(Dimens.sm))
                            Text(
                                text = "An interactive explorer of architectural experiments, production libraries, and open-source systems.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 26.sp
                            )
                        }
                        Spacer(Modifier.width(Dimens.xl))
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .height(110.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = Dimens.md, vertical = Dimens.sm),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${state.filteredProjects.size}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GitBlue,
                                    fontFamily = JetBrainsMono
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = stringResource(Res.string.projects_label),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = JetBrainsMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    Column {
                        Text(
                            text = "~.view_projects.sh",
                            style = MaterialTheme.typography.labelMedium,
                            color = GitGreen,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(Dimens.sm))
                        Text(
                            text = "Projects Gallery",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(Dimens.sm))
                        Text(
                            text = "An interactive explorer of architectural experiments, production libraries, and open-source systems.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 26.sp
                        )
                        Spacer(Modifier.height(Dimens.md))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, BorderColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = Dimens.md, vertical = Dimens.sm),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${state.filteredProjects.size}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GitBlue,
                                    fontFamily = JetBrainsMono
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = stringResource(Res.string.projects_label),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = JetBrainsMono,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(Dimens.xl))

            // ── Tag Filters (Sleek Cyberpunk Filter Chips) ───────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.filter_by_tech),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.sm),
                    verticalArrangement = Arrangement.spacedBy(Dimens.sm),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TagFilterChip(
                        tag = "ALL",
                        isSelected = state.selectedTag == null,
                        onClick = { viewModel.onEvent(ProjectsContract.Event.FilterByTag(null)) }
                    )
                    state.availableTags.forEach { tag ->
                        TagFilterChip(
                            tag = tag,
                            isSelected = state.selectedTag == tag,
                            onClick = { viewModel.onEvent(ProjectsContract.Event.FilterByTag(tag)) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens.lg))

            // ── Grouped Project Sections ─────────────────────────────────
            AnimatedContent(
                targetState = groupedProjects,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "projectSections",
            ) { groups ->
                if (groups.isEmpty()) {
                    Box(Modifier.fillMaxWidth().height(200.dp), Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.no_projects_match),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = JetBrainsMono
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
                        groups.keys.forEach { groupType ->
                            val projectsInGroup = groups[groupType] ?: emptyList()
                            if (projectsInGroup.isNotEmpty()) {
                                SectionHeader(title = "../${groupType.displayName.lowercase()}")
                                Spacer(Modifier.height(Dimens.xs))
                                
                                BoxWithConstraints(Modifier.fillMaxWidth()) {
                                    val columns = when {
                                        maxWidth > 1200.dp -> 4
                                        maxWidth > 850.dp -> 3
                                        maxWidth > 550.dp -> 2
                                        else -> 1
                                    }
                                    
                                    if (columns > 1) {
                                        val rows = projectsInGroup.chunked(columns)
                                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
                                            rows.forEach { rowItems ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                                    horizontalArrangement = Arrangement.spacedBy(Dimens.md)
                                                ) {
                                                    rowItems.forEach { p ->
                                                        TerminalProjectCard(
                                                            project = p,
                                                            modifier = Modifier.weight(1f).fillMaxHeight(),
                                                            onOpenRepo = { viewModel.onEvent(ProjectsContract.Event.ProjectClicked(p)) },
                                                            onOpenDemo = { viewModel.onEvent(ProjectsContract.Event.DemoClicked(p)) },
                                                            onClick = { onNavigateTo(Routes.ProjectDetails(p.id)) }
                                                        )
                                                    }
                                                    repeat(columns - rowItems.size) {
                                                        Spacer(Modifier.weight(1f))
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
                                            projectsInGroup.forEach { p ->
                                                TerminalProjectCard(
                                                    project = p,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    onOpenRepo = { viewModel.onEvent(ProjectsContract.Event.ProjectClicked(p)) },
                                                    onOpenDemo = { viewModel.onEvent(ProjectsContract.Event.DemoClicked(p)) },
                                                    onClick = { onNavigateTo(Routes.ProjectDetails(p.id)) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(Dimens.lg))
        }
        NeonVerticalScrollbar(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
private fun TagFilterChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val neutralBg = MaterialTheme.colorScheme.background
    
    val textColor = if (isSelected) neutralBg else primaryColor
    val backgroundColor = if (isSelected) primaryColor else Color.Transparent
    val borderModifier = if (isSelected) Modifier else Modifier.border(1.dp, primaryColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .then(borderModifier)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (tag == "ALL") "ALL" else "#$tag",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMono,
                color = GitGreen,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(end = 12.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(GitGreen.copy(alpha = 0.3f))
        )
    }
}
