package com.meta.quicklauncher.domain.usecase

import com.meta.quicklauncher.domain.model.AppInfo
import me.xdrop.fuzzywuzzy.FuzzySearch
import javax.inject.Inject

class SearchAppsUseCase @Inject constructor() {
    operator fun invoke(apps: List<AppInfo>, query: String): List<AppInfo> {
        if (query.isBlank()) return apps

        return apps.map { app ->
            val nameScore = FuzzySearch.ratio(query.lowercase(), app.appName.lowercase())
            val packageScore = FuzzySearch.ratio(query.lowercase(), app.packageName.lowercase())
            val bestScore = maxOf(nameScore, packageScore)
            app to bestScore
        }
        .filter { (_, score) -> score >= 50 } // Minimum similarity threshold
        .sortedByDescending { (_, score) -> score }
        .map { (app, _) -> app }
    }
}