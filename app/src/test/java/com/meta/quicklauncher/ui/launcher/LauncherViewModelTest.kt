package com.meta.quicklauncher.ui.launcher

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meta.quicklauncher.domain.model.AppInfo
import com.meta.quicklauncher.domain.usecase.GetInstalledAppsUseCase
import com.meta.quicklauncher.domain.usecase.SearchAppsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LauncherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val mockContext = mockk<Context>()
    private val mockGetInstalledAppsUseCase = mockk<GetInstalledAppsUseCase>()
    private val mockSearchAppsUseCase = mockk<SearchAppsUseCase>()

    private lateinit var viewModel: LauncherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = LauncherViewModel(
            context = mockContext,
            getInstalledAppsUseCase = mockGetInstalledAppsUseCase,
            searchAppsUseCase = mockSearchAppsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads apps successfully`() = runTest {
        val testApps = listOf(
            AppInfo("com.example.app1", "App One", null),
            AppInfo("com.example.app2", "App Two", null)
        )

        coEvery { mockGetInstalledAppsUseCase() } returns testApps

        // Wait for initialization
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(testApps, viewModel.allApps.value)
        assertEquals(testApps, viewModel.filteredApps.value)
        assertEquals("", viewModel.searchQuery.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `search query updates filtered apps`() = runTest {
        val allApps = listOf(
            AppInfo("com.example.calculator", "Calculator", null),
            AppInfo("com.example.calendar", "Calendar", null),
            AppInfo("com.example.camera", "Camera", null)
        )

        val searchResults = listOf(
            AppInfo("com.example.calculator", "Calculator", null),
            AppInfo("com.example.calendar", "Calendar", null)
        )

        coEvery { mockGetInstalledAppsUseCase() } returns allApps
        coEvery { mockSearchAppsUseCase(allApps, "calc") } returns searchResults

        // Initialize
        testDispatcher.scheduler.advanceUntilIdle()

        // Perform search
        viewModel.onSearchQueryChanged("calc")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("calc", viewModel.searchQuery.value)
        assertEquals(searchResults, viewModel.filteredApps.value)
    }

    @Test
    fun `empty search query shows all apps`() = runTest {
        val allApps = listOf(
            AppInfo("com.example.app1", "App One", null),
            AppInfo("com.example.app2", "App Two", null)
        )

        coEvery { mockGetInstalledAppsUseCase() } returns allApps

        // Initialize
        testDispatcher.scheduler.advanceUntilIdle()

        // Search with empty query
        viewModel.onSearchQueryChanged("")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("", viewModel.searchQuery.value)
        assertEquals(allApps, viewModel.filteredApps.value)
    }

    @Test
    fun `blank search query shows all apps`() = runTest {
        val allApps = listOf(
            AppInfo("com.example.app1", "App One", null),
            AppInfo("com.example.app2", "App Two", null)
        )

        coEvery { mockGetInstalledAppsUseCase() } returns allApps

        // Initialize
        testDispatcher.scheduler.advanceUntilIdle()

        // Search with blank query
        viewModel.onSearchQueryChanged("   ")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("   ", viewModel.searchQuery.value)
        assertEquals(allApps, viewModel.filteredApps.value)
    }

    @Test
    fun `handles app loading error gracefully`() = runTest {
        coEvery { mockGetInstalledAppsUseCase() } throws RuntimeException("Package manager error")

        // Initialize
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.allApps.value.isEmpty())
        assertTrue(viewModel.filteredApps.value.isEmpty())
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `loading state is managed correctly`() = runTest {
        val testApps = listOf(AppInfo("com.example.app", "Test App", null))

        coEvery { mockGetInstalledAppsUseCase() } returns testApps

        // Initially loading should be true during initialization
        assertEquals(true, viewModel.isLoading.value)

        // After completion, loading should be false
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(false, viewModel.isLoading.value)
    }
}