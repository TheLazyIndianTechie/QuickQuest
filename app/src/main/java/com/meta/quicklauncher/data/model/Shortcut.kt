package com.meta.quicklauncher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Shortcut(
    val id: String,
    val name: String,
    val packageName: String? = null, // null for custom actions
    val action: ShortcutAction,
    val iconResId: String? = null, // resource identifier or URL
    val order: Int = 0
)

@Serializable
sealed class ShortcutAction {
    @Serializable
    data class LaunchApp(val packageName: String) : ShortcutAction()

    @Serializable
    data class OpenUrl(val url: String) : ShortcutAction()

    @Serializable
    data class CustomIntent(val action: String, val data: String? = null) : ShortcutAction()
}