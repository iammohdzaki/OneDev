package com.one.dev.presentation.ui.about.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.dev.data.models.ProfileStats
import com.one.dev.presentation.ui.theme.BorderColor
import com.one.dev.presentation.ui.theme.Dimens
import com.one.dev.presentation.ui.theme.GitBlue
import com.one.dev.presentation.ui.theme.JetBrainsMono

@Composable
fun StatsGrid(stats: ProfileStats, modifier: Modifier = Modifier) {
    val cells = listOf(
        "${stats.yearsExperience}+" to "YEARS",
        "${stats.projectsBuilt}+"  to "PROJECTS",
        "${stats.articlesWritten}" to "ARTICLES",
        stats.commitsThisYear       to "COMMITS",
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
        cells.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(Dimens.md)) {
                row.forEach { (value, label) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
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
                                text = value,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = GitBlue,
                                fontFamily = JetBrainsMono
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = label,
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
    }
}
