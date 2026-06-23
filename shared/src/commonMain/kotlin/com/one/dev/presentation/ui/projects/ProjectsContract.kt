package com.one.dev.presentation.ui.projects

import com.one.dev.data.models.PortfolioProject
import com.one.dev.data.models.ProjectGroup
import com.one.dev.data.models.ProjectType
import com.one.dev.presentation.mvi.UiEffect
import com.one.dev.presentation.mvi.UiEvent
import com.one.dev.presentation.mvi.UiState

object ProjectsContract {

    data class State(
        val allProjects: List<PortfolioProject> = emptyList(),
        val filteredProjects: List<PortfolioProject> = emptyList(),
        val selectedType: ProjectType? = null,
        val selectedGroup: ProjectGroup? = null,
        val selectedTag: String? = null,
        val availableTags: List<String> = emptyList(),
        val isLoading: Boolean = true,
    ) : UiState

    sealed interface Event : UiEvent {
        data object ScreenOpened : Event
        data class FilterByType(val type: ProjectType?) : Event
        data class FilterByGroup(val group: ProjectGroup?) : Event
        data class FilterByTag(val tag: String?) : Event
        data class ProjectClicked(val project: PortfolioProject) : Event
        data class DemoClicked(val project: PortfolioProject) : Event
    }

    sealed interface Effect : UiEffect {
        data class OpenUrl(val url: String) : Effect
    }
}
