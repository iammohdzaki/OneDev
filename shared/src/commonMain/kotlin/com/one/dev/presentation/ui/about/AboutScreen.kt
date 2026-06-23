package com.one.dev.presentation.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.one.dev.data.LocalPortfolioRepository
import com.one.dev.data.models.PortfolioProfile
import com.one.dev.presentation.ui.about.components.ExperienceSection
import com.one.dev.presentation.ui.about.components.HeroSection
import com.one.dev.presentation.ui.about.components.SkillsSection
import com.one.dev.presentation.ui.about.components.StatsGrid
import com.one.dev.presentation.ui.theme.BgPrimary
import com.one.dev.presentation.ui.theme.Dimens

@Composable
fun AboutScreen() {
    var profile by remember { mutableStateOf(PortfolioProfile.EMPTY) }
    LaunchedEffect(Unit) {
        profile = LocalPortfolioRepository.getProfile()
    }
    val skills = profile.skillBars
    val experience = profile.experiences
    val philosophy = profile.philosophy

    Box(Modifier.fillMaxSize().background(BgPrimary)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.screenHorizontal)
                .padding(bottom = Dimens.contentBottomPad),
        ) {
            Spacer(Modifier.height(Dimens.xl))

            // ── Hero + Stats ───────────────────────────────────────────────
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val isWide = maxWidth > 700.dp
                if (isWide) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.Top) {
                        HeroSection(profile.name, profile.tagline, profile.bio, profile.email, profile.cvUrl, Modifier.weight(1f))
                        Spacer(Modifier.width(Dimens.xl))
                        StatsGrid(profile.stats, Modifier.widthIn(min = 280.dp, max = 320.dp))
                    }
                } else {
                    Column {
                        HeroSection(profile.name, profile.tagline, profile.bio, profile.email, profile.cvUrl, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(Dimens.xl))
                        StatsGrid(profile.stats, Modifier.fillMaxWidth())
                    }
                }
            }

            Spacer(Modifier.height(Dimens.xxl))

            // ── Skills + Experience ────────────────────────────────────────
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val isWide = maxWidth > 700.dp
                if (isWide) {
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(Dimens.xl), Alignment.Top) {
                        Column(Modifier.weight(0.4f)) {
                            SkillsSection(skills, profile.skills, philosophy)
                        }
                        Column(Modifier.weight(0.6f)) {
                            ExperienceSection(experience)
                        }
                    }
                } else {
                    Column {
                        SkillsSection(skills, profile.skills, philosophy)
                        Spacer(Modifier.height(Dimens.xl))
                        ExperienceSection(experience)
                    }
                }
            }
            Spacer(Modifier.height(Dimens.lg))
        }
    }
}
