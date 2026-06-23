package com.one.dev.presentation.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.one.dev.data.models.PortfolioProfile

/**
 * Modern shell layout:
 * - Top: Status TopBar (tabbed header navigation + current shell path)
 * - Center: Page Content (takes full screen width/height)
 * - Footer: Muted Status Line (BottomStatusBar)
 */
@Composable
fun AppShell(
    current    : Any,
    onNavigate : (Any) -> Unit,
    profile    : PortfolioProfile,
    topBarPath : String   = "",
    modifier   : Modifier = Modifier,
    content    : @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fixes background leakage during tab transition
    ) {
        TopBar(
            currentRoute = current,
            onNavigate   = onNavigate,
            profile      = profile,
            pathLabel    = topBarPath,
            modifier     = Modifier.fillMaxWidth()
        )
        Box(Modifier.weight(1f)) {
            // Render main page content
            content()
        }
        BottomStatusBar(
            profile = profile,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
