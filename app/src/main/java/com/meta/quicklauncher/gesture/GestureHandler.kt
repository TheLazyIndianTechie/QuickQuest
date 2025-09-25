package com.meta.quicklauncher.gesture

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.meta.quicklauncher.QuickQuestApplication
import com.meta.quicklauncher.feedback.HapticFeedback
import com.oculus.interaction.OVRGestureListener
import com.oculus.interaction.OVRGestureManager
import com.oculus.interaction.gesture.PinchGesture
import com.oculus.interaction.gesture.WristTapGesture
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GestureHandler @Inject constructor(
    @com.meta.quicklauncher.di.ApplicationContext private val context: Context,
    private val hapticFeedback: HapticFeedback
) : LifecycleObserver, OVRGestureListener {

    private var gestureManager: OVRGestureManager? = null
    private var onGestureDetected: ((GestureType) -> Unit)? = null

    enum class GestureType {
        PINCH,
        WRIST_TAP,
        MENU_BUTTON_DOUBLE_TAP
    }

    fun initialize() {
        gestureManager = OVRGestureManager(context)
        gestureManager?.addGestureListener(this)

        // Register gestures
        gestureManager?.registerGesture(PinchGesture::class.java)
        gestureManager?.registerGesture(WristTapGesture::class.java)
    }

    fun setOnGestureDetectedListener(listener: (GestureType) -> Unit) {
        onGestureDetected = listener
    }

    override fun onGestureDetected(gesture: Any?) {
        when (gesture) {
            is PinchGesture -> {
                hapticFeedback.performGestureFeedback()
                onGestureDetected?.invoke(GestureType.PINCH)
            }
            is WristTapGesture -> {
                hapticFeedback.performGestureFeedback()
                onGestureDetected?.invoke(GestureType.WRIST_TAP)
            }
        }
    }

    override fun onGestureEnded(gesture: Any?) {
        // Handle gesture end if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        gestureManager?.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        gestureManager?.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        gestureManager?.removeGestureListener(this)
        gestureManager?.shutdown()
        gestureManager = null
    }
}