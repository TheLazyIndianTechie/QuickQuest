package com.meta.quicklauncher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.meta.quicklauncher.gesture.GestureHandler
import com.meta.quicklauncher.ui.launcher.LauncherViewModel
import com.meta.quicklauncher.ui.theme.QuickQuestTheme
import com.oculus.spatial.spatialactivity.SpatialActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SpatialMainActivity : SpatialActivity() {

    @Inject
    lateinit var gestureHandler: GestureHandler

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize gesture handler
        gestureHandler.initialize()
        lifecycle.addObserver(gestureHandler)

        // Set up gesture detection
        gestureHandler.setOnGestureDetectedListener { gestureType ->
            lifecycleScope.launch {
                when (gestureType) {
                    GestureHandler.GestureType.PINCH -> {
                        // Toggle launcher visibility
                        toggleLauncherVisibility()
                    }
                    GestureHandler.GestureType.WRIST_TAP -> {
                        // Could be used for other actions
                    }
                    GestureHandler.GestureType.MENU_BUTTON_DOUBLE_TAP -> {
                        // Show launcher
                        showLauncher()
                    }
                }
            }
        }

        setContent {
            QuickQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuickQuestApp()
                }
            }
        }
    }

    private fun toggleLauncherVisibility() {
        // TODO: Implement launcher visibility toggle
    }

    private fun showLauncher() {
        // TODO: Implement launcher show logic
    }
}