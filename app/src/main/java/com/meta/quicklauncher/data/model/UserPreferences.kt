package com.meta.quicklauncher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val theme: String = "auto", // auto, light, dark
    val showSystemApps: Boolean = false,
    val maxSearchResults: Int = 20,
    val enableHapticFeedback: Boolean = true,
    val autoHideDelay: Long = 5000L // milliseconds
)