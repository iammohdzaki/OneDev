package com.one.dev.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Article
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

/**
 * Unified navigation routes and metadata.
 */
object Routes {
    @Serializable
    object Projects

    @Serializable
    data class ProjectDetails(val id: String)

    @Serializable
    object Blog

    @Serializable
    object About

    /**
     * Holds metadata for top-level dock destinations.
     */
    data class Destination(
        val route: Any,
        val label: String,
        val icon: ImageVector,
        val shellPath: String
    )

    /**
     * List of all primary destinations shown in the dock.
     */
    val mainDestinations = listOf(
        Destination(route = About, label = "Me", icon = Icons.Rounded.Person, shellPath = "dev@onedev ~ $ cat about.md"),
        Destination(route = Projects, label = "Projects", icon = Icons.Rounded.Folder, shellPath = "root@onedev ~/projects %")
    )

    /**
     * Determines the terminal prompt prefix path based on the current active route.
     */
    fun getShellPath(route: Any?): String {
        if (route == null) return "dev@onedev ~ $ cat about.md"
        
        // Find matching static top-level destination
        val staticDest = mainDestinations.firstOrNull { it.route::class == route::class }
        if (staticDest != null) return staticDest.shellPath

        // Handle parameterised routes
        return when (route) {
            is ProjectDetails -> "root@onedev ~/projects/${route.id} %"
            else -> "dev@onedev ~ $ cat about.md"
        }
    }
}
