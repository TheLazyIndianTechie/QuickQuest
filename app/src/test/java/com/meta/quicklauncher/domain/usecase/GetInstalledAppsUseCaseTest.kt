package com.meta.quicklauncher.domain.usecase

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.meta.quicklauncher.domain.model.AppInfo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GetInstalledAppsUseCaseTest {

    private val mockContext = mockk<Context>()
    private val mockPackageManager = mockk<PackageManager>()
    private val useCase = GetInstalledAppsUseCase(mockContext)

    init {
        every { mockContext.packageManager } returns mockPackageManager
    }

    @Test
    fun `returns empty list when no packages installed`() {
        every { mockPackageManager.getInstalledPackages(any<Int>()) } returns emptyList()

        val result = useCase()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `converts package info to app info correctly`() {
        val packageInfo = PackageInfo().apply {
            packageName = "com.example.test"
            applicationInfo = ApplicationInfo().apply {
                loadLabel(mockPackageManager).toString() returns "Test App"
                loadIcon(mockPackageManager) returns null
                flags = 0 // Not a system app
            }
        }

        every { mockPackageManager.getInstalledPackages(any<Int>()) } returns listOf(packageInfo)
        every { packageInfo.applicationInfo?.loadLabel(mockPackageManager) } returns "Test App"
        every { packageInfo.applicationInfo?.loadIcon(mockPackageManager) } returns null

        val result = useCase()

        assertEquals(1, result.size)
        val appInfo = result.first()
        assertEquals("com.example.test", appInfo.packageName)
        assertEquals("Test App", appInfo.appName)
        assertFalse(appInfo.isSystemApp)
    }

    @Test
    fun `identifies system apps correctly`() {
        val packageInfo = PackageInfo().apply {
            packageName = "com.example.system"
            applicationInfo = ApplicationInfo().apply {
                loadLabel(mockPackageManager).toString() returns "System App"
                loadIcon(mockPackageManager) returns null
                flags = android.content.pm.ApplicationInfo.FLAG_SYSTEM
            }
        }

        every { mockPackageManager.getInstalledPackages(any<Int>()) } returns listOf(packageInfo)
        every { packageInfo.applicationInfo?.loadLabel(mockPackageManager) } returns "System App"
        every { packageInfo.applicationInfo?.loadIcon(mockPackageManager) } returns null

        val result = useCase()

        assertEquals(1, result.size)
        assertTrue(result.first().isSystemApp)
    }

    @Test
    fun `handles null application info gracefully`() {
        val packageInfo = PackageInfo().apply {
            packageName = "com.example.null"
            applicationInfo = null
        }

        every { mockPackageManager.getInstalledPackages(any<Int>()) } returns listOf(packageInfo)

        val result = useCase()

        assertEquals(1, result.size)
        val appInfo = result.first()
        assertEquals("com.example.null", appInfo.packageName)
        assertEquals("com.example.null", appInfo.appName) // Falls back to package name
    }

    @Test
    fun `results are sorted by app name`() {
        val packageInfo1 = PackageInfo().apply {
            packageName = "com.example.b"
            applicationInfo = ApplicationInfo().apply {
                loadLabel(mockPackageManager).toString() returns "B App"
                loadIcon(mockPackageManager) returns null
                flags = 0
            }
        }

        val packageInfo2 = PackageInfo().apply {
            packageName = "com.example.a"
            applicationInfo = ApplicationInfo().apply {
                loadLabel(mockPackageManager).toString() returns "A App"
                loadIcon(mockPackageManager) returns null
                flags = 0
            }
        }

        every { mockPackageManager.getInstalledPackages(any<Int>()) } returns listOf(packageInfo1, packageInfo2)
        every { packageInfo1.applicationInfo?.loadLabel(mockPackageManager) } returns "B App"
        every { packageInfo1.applicationInfo?.loadIcon(mockPackageManager) } returns null
        every { packageInfo2.applicationInfo?.loadLabel(mockPackageManager) } returns "A App"
        every { packageInfo2.applicationInfo?.loadIcon(mockPackageManager) } returns null

        val result = useCase()

        assertEquals(2, result.size)
        assertEquals("A App", result.first().appName)
        assertEquals("B App", result.last().appName)
    }
}