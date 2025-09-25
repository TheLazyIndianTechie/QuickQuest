package com.meta.quicklauncher.domain.usecase

import com.meta.quicklauncher.domain.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchAppsUseCaseTest {

    private val useCase = SearchAppsUseCase()

    @Test
    fun `search with empty query returns all apps`() {
        val apps = listOf(
            AppInfo("com.example.app1", "App One", null),
            AppInfo("com.example.app2", "App Two", null)
        )

        val result = useCase(apps, "")

        assertEquals(apps, result)
    }

    @Test
    fun `search with blank query returns all apps`() {
        val apps = listOf(
            AppInfo("com.example.app1", "App One", null),
            AppInfo("com.example.app2", "App Two", null)
        )

        val result = useCase(apps, "   ")

        assertEquals(apps, result)
    }

    @Test
    fun `search by app name returns matching apps`() {
        val apps = listOf(
            AppInfo("com.example.app1", "Calculator", null),
            AppInfo("com.example.app2", "Calendar", null),
            AppInfo("com.example.app3", "Camera", null)
        )

        val result = useCase(apps, "calc")

        assertEquals(2, result.size)
        assertTrue(result.any { it.appName == "Calculator" })
        assertTrue(result.any { it.appName == "Calendar" })
    }

    @Test
    fun `search by package name returns matching apps`() {
        val apps = listOf(
            AppInfo("com.google.calculator", "Calculator", null),
            AppInfo("com.example.calendar", "Calendar", null),
            AppInfo("com.example.camera", "Camera", null)
        )

        val result = useCase(apps, "google")

        assertEquals(1, result.size)
        assertEquals("Calculator", result.first().appName)
    }

    @Test
    fun `search with low similarity score filters out results`() {
        val apps = listOf(
            AppInfo("com.example.app1", "Calculator", null),
            AppInfo("com.example.app2", "Calendar", null)
        )

        val result = useCase(apps, "xyz")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `search results are ordered by similarity score descending`() {
        val apps = listOf(
            AppInfo("com.example.app1", "Calculator", null),
            AppInfo("com.example.app2", "Calendar App", null),
            AppInfo("com.example.app3", "Camera", null)
        )

        val result = useCase(apps, "calc")

        assertEquals(2, result.size)
        // Calculator should come before Calendar App due to better match
        assertEquals("Calculator", result.first().appName)
        assertEquals("Calendar App", result.last().appName)
    }

    @Test
    fun `search is case insensitive`() {
        val apps = listOf(
            AppInfo("com.example.app1", "Calculator", null),
            AppInfo("com.example.app2", "CALENDAR", null)
        )

        val result = useCase(apps, "calculator")

        assertEquals(2, result.size)
    }
}