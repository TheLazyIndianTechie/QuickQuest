package com.meta.quicklauncher

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.meta.quicklauncher.ui.launcher.LauncherScreen
import com.meta.quicklauncher.ui.launcher.LauncherViewModel
import com.meta.quicklauncher.ui.spatial.SpatialLauncherScreen
import com.oculus.spatial.spatialactivity.SpatialActivity

@Composable
fun QuickQuestApp() {
    val context = LocalContext.current
    val viewModel: LauncherViewModel = hiltViewModel()

    // Use Spatial UI if running in SpatialActivity (Quest), otherwise use regular UI
    if (context is SpatialActivity) {
        SpatialLauncherScreen(viewModel = viewModel)
    } else {
        LauncherScreen(viewModel = viewModel)
    }
}