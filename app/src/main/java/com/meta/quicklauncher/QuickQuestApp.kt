package com.meta.quicklauncher

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.meta.quicklauncher.ui.launcher.LauncherScreen
import com.meta.quicklauncher.ui.launcher.LauncherViewModel

@Composable
fun QuickQuestApp() {
    val viewModel: LauncherViewModel = hiltViewModel()

    LauncherScreen(viewModel = viewModel)
}