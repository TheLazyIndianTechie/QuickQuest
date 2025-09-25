package com.meta.quicklauncher.data.repository

import android.content.Context
import com.meta.quicklauncher.data.model.Shortcut
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val shortcutsFile = File(context.filesDir, "shortcuts.json")
    private val json = Json { prettyPrint = true }

    fun getShortcuts(): Flow<List<Shortcut>> = flow {
        try {
            val shortcuts = withContext(Dispatchers.IO) {
                if (shortcutsFile.exists()) {
                    val jsonString = shortcutsFile.readText()
                    json.decodeFromString<List<Shortcut>>(jsonString)
                } else {
                    // Return default shortcuts
                    getDefaultShortcuts()
                }
            }
            emit(shortcuts)
        } catch (e: Exception) {
            // Return default shortcuts on error
            emit(getDefaultShortcuts())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveShortcuts(shortcuts: List<Shortcut>) {
        withContext(Dispatchers.IO) {
            val jsonString = json.encodeToString(shortcuts)
            shortcutsFile.writeText(jsonString)
        }
    }

    suspend fun addShortcut(shortcut: Shortcut) {
        val currentShortcuts = getShortcuts().firstOrNull() ?: emptyList()
        val updatedShortcuts = currentShortcuts + shortcut
        saveShortcuts(updatedShortcuts)
    }

    suspend fun removeShortcut(shortcutId: String) {
        val currentShortcuts = getShortcuts().firstOrNull() ?: emptyList()
        val updatedShortcuts = currentShortcuts.filter { it.id != shortcutId }
        saveShortcuts(updatedShortcuts)
    }

    suspend fun updateShortcutOrder(shortcutId: String, newOrder: Int) {
        val currentShortcuts = getShortcuts().firstOrNull() ?: emptyList()
        val updatedShortcuts = currentShortcuts.map { shortcut ->
            if (shortcut.id == shortcutId) {
                shortcut.copy(order = newOrder)
            } else {
                shortcut
            }
        }.sortedBy { it.order }
        saveShortcuts(updatedShortcuts)
    }

    private fun getDefaultShortcuts(): List<Shortcut> {
        return listOf(
            Shortcut(
                id = "settings",
                name = "Settings",
                action = com.meta.quicklauncher.data.model.ShortcutAction.CustomIntent(
                    action = "android.settings.SETTINGS"
                ),
                order = 0
            ),
            Shortcut(
                id = "wifi",
                name = "Wi-Fi Settings",
                action = com.meta.quicklauncher.data.model.ShortcutAction.CustomIntent(
                    action = "android.settings.WIFI_SETTINGS"
                ),
                order = 1
            )
        )
    }

    private suspend fun <T> Flow<T>.firstOrNull(): T? {
        var result: T? = null
        collect { value ->
            result = value
        }
        return result
    }
}