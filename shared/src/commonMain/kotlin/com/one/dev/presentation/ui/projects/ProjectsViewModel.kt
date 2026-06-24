package com.one.dev.presentation.ui.projects

import com.one.dev.data.LocalPortfolioRepository
import com.one.dev.data.models.ProjectGroup
import com.one.dev.data.models.ProjectType
import com.one.dev.presentation.mvi.BaseViewModel
import kotlinx.coroutines.launch

class ProjectsViewModel : BaseViewModel<ProjectsContract.State, ProjectsContract.Event, ProjectsContract.Effect>() {

    override fun createInitialState() = ProjectsContract.State()

    init { onEvent(ProjectsContract.Event.ScreenOpened) }

    override fun onEvent(event: ProjectsContract.Event) {
        when (event) {
            ProjectsContract.Event.ScreenOpened       -> loadProjects()
            is ProjectsContract.Event.FilterByType    -> applyTypeFilter(event.type)
            is ProjectsContract.Event.FilterByGroup   -> applyGroupFilter(event.group)
            is ProjectsContract.Event.FilterByTag     -> applyTagFilter(event.tag)
            is ProjectsContract.Event.ProjectClicked  -> sendEffect { ProjectsContract.Effect.OpenUrl(event.project.githubUrl) }
            is ProjectsContract.Event.DemoClicked     -> event.project.demoUrl?.let { url -> sendEffect { ProjectsContract.Effect.OpenUrl(url) } }
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val projects = LocalPortfolioRepository.getProjects()
                val tags = projects.flatMap { it.techStack }.distinct().sorted()
                setState {
                    copy(
                        allProjects      = projects,
                        filteredProjects = projects,
                        availableTags    = tags,
                        isLoading        = false
                    )
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun applyTypeFilter(type: ProjectType?) {
        val newType = if (state.value.selectedType == type) null else type
        setState {
            copy(
                selectedType     = newType,
                filteredProjects = allProjects.applyFilters(newType, selectedGroup, selectedTag),
            )
        }
    }

    private fun applyGroupFilter(group: ProjectGroup?) {
        val newGroup = if (state.value.selectedGroup == group) null else group
        setState {
            copy(
                selectedGroup    = newGroup,
                filteredProjects = allProjects.applyFilters(selectedType, newGroup, selectedTag),
            )
        }
    }

    private fun applyTagFilter(tag: String?) {
        val newTag = if (state.value.selectedTag == tag) null else tag
        setState {
            copy(
                selectedTag      = newTag,
                filteredProjects = allProjects.applyFilters(selectedType, selectedGroup, newTag),
            )
        }
    }

    private fun List<com.one.dev.data.models.PortfolioProject>.applyFilters(
        type  : ProjectType?,
        group : ProjectGroup?,
        tag   : String?
    ) = filter { p ->
        (type  == null || p.type  == type) &&
        (group == null || p.group == group) &&
        (tag   == null || p.techStack.contains(tag))
    }
}
