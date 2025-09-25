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
        return packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            .map { packageInfo ->
                val appName = packageInfo.applicationInfo?.loadLabel(packageManager)?.toString() ?: packageInfo.packageName
                val icon = packageInfo.applicationInfo?.loadIcon(packageManager)
                val isSystemApp = (packageInfo.applicationInfo?.flags ?: 0) and android.content.pm.ApplicationInfo.FLAG_SYSTEM != 0

                AppInfo(
                    packageName = packageInfo.packageName,
                    appName = appName,
                    icon = icon,
                    isSystemApp = isSystemApp
                )
            }
            .sortedBy { it.appName }
    }
}