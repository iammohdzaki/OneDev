package com.one.dev.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.one.dev.presentation.ui.about.AboutScreen
import com.one.dev.presentation.ui.projects.ProjectDetailsScreen
import com.one.dev.presentation.ui.projects.ProjectsScreen
import com.one.dev.presentation.ui.projects.ProjectsViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.About, // Set "Me" (About) as the landing screen
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        enterTransition = { fadeIn() + slideInHorizontally { it / 4 } },
        exitTransition = { fadeOut() + slideOutHorizontally { -it / 4 } },
        popEnterTransition = { fadeIn() + slideInHorizontally { -it / 4 } },
        popExitTransition = { fadeOut() + slideOutHorizontally { it / 4 } }
    ) {
        composable<Routes.About> {
            AboutScreen()
        }
        composable<Routes.Projects> {
            ProjectsScreen(
                viewModel = ProjectsViewModel(),
                onNavigateTo = { route -> navController.navigate(route) }
            )
        }
        composable<Routes.ProjectDetails> { backStackEntry ->
            val details: Routes.ProjectDetails = backStackEntry.toRoute()
            ProjectDetailsScreen(
                projectId = details.id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
