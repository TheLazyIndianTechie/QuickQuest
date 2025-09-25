package com.meta.quicklauncher.ui.spatial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.meta.quicklauncher.ui.launcher.LauncherViewModel
import com.oculus.spatial.spatialactivity.SpatialActivity
import com.oculus.spatial.spatialui.*

@Composable
fun SpatialLauncherScreen(viewModel: LauncherViewModel) {
    val context = LocalContext.current as SpatialActivity
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredApps by viewModel.filteredApps.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Spatial UI overlay positioned in 3D space
    SpatialSurface(
        modifier = Modifier
            .spatialSize(width = 800.dp, height = 600.dp)
            .spatialPosition(
                position = SpatialPosition(
                    x = 0.dp,
                    y = 0.dp,
                    z = -1000.dp // Position 1 meter in front of user
                ),
                coordinateSystem = SpatialCoordinateSystem.HeadLocked // Head-locked positioning
            )
            .spatialRotation(
                rotation = SpatialRotation(
                    x = 0f,
                    y = 0f,
                    z = 0f
                )
            ),
        shape = SpatialShape.Rectangle(radius = 16.dp),
        passthroughBackground = true // Enable passthrough for minimal UI takeover
    ) {
        // Glass-morphic overlay content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Semi-transparent background
        ) {
            // Main launcher content
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(600.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search bar with spatial interaction
                SpatialTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    placeholder = "Search apps...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // App list
                if (isLoading) {
                    SpatialCircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                } else if (filteredApps.isEmpty()) {
                    SpatialText(
                        text = "No apps found",
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    SpatialLazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredApps) { app ->
                            SpatialAppListItem(
                                appInfo = app,
                                onClick = { viewModel.launchApp(app) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpatialAppListItem(
    appInfo: com.meta.quicklauncher.domain.model.AppInfo,
    onClick: () -> Unit
) {
    SpatialSurface(
        modifier = Modifier
            .fillMaxWidth()
            .spatialClickable(onClick = onClick),
        shape = SpatialShape.Rectangle(radius = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.1f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App icon placeholder (would load actual icon)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // App name
            SpatialText(
                text = appInfo.appName,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            // Launch button
            SpatialText(
                text = "Launch",
                color = Color.Cyan
            )
        }
    }
}