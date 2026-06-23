package com.one.dev

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.one.dev.data.LocalPortfolioRepository
import com.one.dev.data.models.PortfolioProfile
import com.one.dev.navigation.AppNavigation
import com.one.dev.navigation.Routes
import com.one.dev.presentation.ui.shell.AppShell
import com.one.dev.presentation.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        val profileState = produceState(initialValue = PortfolioProfile.EMPTY) {
            value = LocalPortfolioRepository.getProfile()
        }
        val profile = profileState.value

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val currentRoute: Any = when {
            currentDestination?.hasRoute<Routes.About>() == true -> Routes.About
            currentDestination?.hasRoute<Routes.Projects>() == true -> Routes.Projects
            currentDestination?.hasRoute<Routes.ProjectDetails>() == true -> {
                navBackStackEntry?.toRoute<Routes.ProjectDetails>() ?: Routes.Projects
            }

            else -> Routes.About // Default to Me page (About)
        }

        val topBarPath = Routes.getShellPath(currentRoute)

        AppShell(
            current = currentRoute,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            profile = profile,
            topBarPath = topBarPath,
        ) {
            AppNavigation(
                navController = navController
            )
        }
    }
}