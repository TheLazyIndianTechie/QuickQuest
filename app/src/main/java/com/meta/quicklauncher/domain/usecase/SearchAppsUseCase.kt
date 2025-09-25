package com.meta.quicklauncher.domain.usecase

import com.meta.quicklauncher.domain.model.AppInfo
import me.xdrop.fuzzywuzzy.FuzzySearch
import javax.inject.Inject

class SearchAppsUseCase @Inject constructor() {
    operator fun invoke(apps: List<AppInfo>, query: String): List<AppInfo> {
        if (query.isBlank()) return apps

        val normalizedQuery = query.lowercase().trim()

        // Early exit for very short queries to improve performance
        if (normalizedQuery.length < 2) {
            return apps.filter { app ->
                app.appName.lowercase().startsWith(normalizedQuery) ||
                app.packageName.lowercase().startsWith(normalizedQuery)
            }
        }

        return apps.map { app ->
            // Use weighted scoring: app name matches are more important than package name
            val nameScore = FuzzySearch.ratio(normalizedQuery, app.appName.lowercase())
            val packageScore = FuzzySearch.ratio(normalizedQuery, app.packageName.lowercase()) * 0.7 // Weight package matches lower
            val bestScore = maxOf(nameScore, packageScore.toInt())
            app to bestScore
        }
        .filter { (_, score) -> score >= 60 } // Increased threshold for better relevance
        .sortedByDescending { (_, score) -> score }
        .map { (app, _) -> app }
        .take(20) // Limit results to top 20 for performance
    }
}