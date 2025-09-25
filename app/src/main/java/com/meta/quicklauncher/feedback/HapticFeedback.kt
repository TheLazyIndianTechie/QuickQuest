package com.meta.quicklauncher.feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedback @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService<VibratorManager>()
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService<Vibrator>()
    }

    fun performGestureFeedback() {
        // Light vibration for gesture detection
        vibrate(50L, VibrationEffect.DEFAULT_AMPLITUDE / 2)
    }

    fun performLaunchFeedback() {
        // Medium vibration for app launch
        vibrate(100L, VibrationEffect.DEFAULT_AMPLITUDE)
    }

    fun performErrorFeedback() {
        // Double vibration pattern for errors
        vibrate(longArrayOf(0, 100, 50, 100), intArrayOf(0, 50, 0, 50))
    }

    fun performSuccessFeedback() {
        // Success pattern
        vibrate(longArrayOf(0, 50, 50, 50), intArrayOf(0, 100, 0, 50))
    }

    private fun vibrate(duration: Long, amplitude: Int) {
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(duration, amplitude))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(duration)
            }
        }
    }

    private fun vibrate(pattern: LongArray, amplitudes: IntArray) {
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(pattern, -1)
            }
        }
    }
}