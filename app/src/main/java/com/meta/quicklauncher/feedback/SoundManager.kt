package com.meta.quicklauncher.feedback

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var toneGenerator: ToneGenerator? = null

    init {
        initializeToneGenerator()
    }

    private fun initializeToneGenerator() {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 30) // Low volume
        } catch (e: Exception) {
            // Handle case where ToneGenerator can't be created
            toneGenerator = null
        }
    }

    fun playGestureSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }

    fun playLaunchSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 150)
    }

    fun playErrorSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 200)
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}