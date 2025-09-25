package com.meta.quicklauncher.ui.launcher

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meta.quicklauncher.data.model.UserPreferences
import com.meta.quicklauncher.data.repository.UserPreferencesRepository
import com.meta.quicklauncher.domain.model.AppInfo
import com.meta.quicklauncher.domain.usecase.GetInstalledAppsUseCase
import com.meta.quicklauncher.domain.usecase.SearchAppsUseCase
import com.meta.quicklauncher.feedback.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getInstalledAppsUseCase: GetInstalledAppsUseCase,
    private val searchAppsUseCase: SearchAppsUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val hapticFeedback: HapticFeedback
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _filteredApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val filteredApps: StateFlow<List<AppInfo>> = _filteredApps.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow

    init {
        loadInstalledApps()
        setupSearchObserver()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load apps on IO dispatcher for better performance
                val apps = withContext(Dispatchers.IO) {
                    getInstalledAppsUseCase()
                }
                _allApps.value = apps
                _filteredApps.value = apps
            } catch (e: Exception) {
                // Handle error
                _allApps.value = emptyList()
                _filteredApps.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setupSearchObserver() {
        // Debounce search queries to avoid excessive filtering
        viewModelScope.launch {
            _searchQuery
                .debounce(150) // 150ms debounce for smooth typing experience
                .distinctUntilChanged()
                .collect { query ->
                    _filteredApps.value = if (query.isBlank()) {
                        _allApps.value
                    } else {
                        // Perform search on Default dispatcher for CPU-intensive work
                        withContext(Dispatchers.Default) {
                            searchAppsUseCase(_allApps.value, query)
                        }
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun launchApp(appInfo: AppInfo) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
                hapticFeedback.performLaunchFeedback()
            }
        } catch (e: Exception) {
            hapticFeedback.performErrorFeedback()
            // Handle launch failure
        }
    }

    // User preferences methods
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateTheme(theme)
        }
    }

    fun updateShowSystemApps(show: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateShowSystemApps(show)
        }
    }

    fun updateMaxSearchResults(max: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateMaxSearchResults(max)
        }
    }
}