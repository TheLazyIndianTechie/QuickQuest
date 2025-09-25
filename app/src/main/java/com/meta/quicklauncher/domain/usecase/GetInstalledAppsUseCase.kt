package com.meta.quicklauncher.domain.usecase

import android.content.Context
import android.content.pm.PackageManager
import com.meta.quicklauncher.domain.model.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetInstalledAppsUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): List<AppInfo> {
        val packageManager = context.packageManager

        // Use GET_ACTIVITIES flag instead of GET_META_DATA for better performance
        // Only get essential package information initially
        return packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            .filter { packageInfo ->
                // Filter out packages without launchable activities to reduce list size
                packageInfo.activities?.isNotEmpty() == true
            }
            .map { packageInfo ->
                val appName = packageInfo.applicationInfo?.loadLabel(packageManager)?.toString() ?: packageInfo.packageName
                // Defer icon loading to reduce initial memory usage - load on demand
                val isSystemApp = (packageInfo.applicationInfo?.flags ?: 0) and android.content.pm.ApplicationInfo.FLAG_SYSTEM != 0

                AppInfo(
                    packageName = packageInfo.packageName,
                    appName = appName,
                    icon = null, // Load icon lazily to reduce memory footprint
                    isSystemApp = isSystemApp
                )
            }
            .sortedBy { it.appName }
    }
}