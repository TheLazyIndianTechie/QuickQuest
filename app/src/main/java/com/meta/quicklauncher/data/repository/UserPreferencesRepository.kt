package com.meta.quicklauncher.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.meta.quicklauncher.data.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    // Preference keys
    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val SHOW_SYSTEM_APPS = booleanPreferencesKey("show_system_apps")
        val MAX_SEARCH_RESULTS = intPreferencesKey("max_search_results")
        val ENABLE_HAPTIC_FEEDBACK = booleanPreferencesKey("enable_haptic_feedback")
        val AUTO_HIDE_DELAY = longPreferencesKey("auto_hide_delay")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                theme = preferences[PreferencesKeys.THEME] ?: "auto",
                showSystemApps = preferences[PreferencesKeys.SHOW_SYSTEM_APPS] ?: false,
                maxSearchResults = preferences[PreferencesKeys.MAX_SEARCH_RESULTS] ?: 20,
                enableHapticFeedback = preferences[PreferencesKeys.ENABLE_HAPTIC_FEEDBACK] ?: true,
                autoHideDelay = preferences[PreferencesKeys.AUTO_HIDE_DELAY] ?: 5000L
            )
        }

    suspend fun updateTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }

    suspend fun updateShowSystemApps(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_SYSTEM_APPS] = show
        }
    }

    suspend fun updateMaxSearchResults(max: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_SEARCH_RESULTS] = max
        }
    }

    suspend fun updateHapticFeedback(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_HAPTIC_FEEDBACK] = enabled
        }
    }

    suspend fun updateAutoHideDelay(delay: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_HIDE_DELAY] = delay
        }
    }
}