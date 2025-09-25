package com.meta.quicklauncher

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.meta.quicklauncher.ui.launcher.LauncherScreen
import com.meta.quicklauncher.ui.launcher.LauncherViewModel
import com.meta.quicklauncher.ui.spatial.SpatialLauncherScreen
import com.oculus.spatial.spatialactivity.SpatialActivity

@Composable
fun QuickQuestApp(isLauncherVisible: Boolean = true) {
    val context = LocalContext.current
    val viewModel: LauncherViewModel = hiltViewModel()

    // Only show launcher if visible (for Quest overlay mode)
    if (!isLauncherVisible) {
        return
    }

    // Use Spatial UI if running in SpatialActivity (Quest), otherwise use regular UI
    if (context is SpatialActivity) {
        SpatialLauncherScreen(viewModel = viewModel)
    } else {
        LauncherScreen(viewModel = viewModel)
    }
}