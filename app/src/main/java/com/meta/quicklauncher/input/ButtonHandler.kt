package com.meta.quicklauncher.input

import android.content.Context
import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class ButtonHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutineScope: CoroutineScope
) : LifecycleObserver {

    private val _buttonEvents = MutableSharedFlow<ButtonEvent>()
    val buttonEvents = _buttonEvents.asSharedFlow()

    private var lastMenuButtonPressTime = 0L
    private val doubleTapThreshold = 300L // milliseconds

    enum class ButtonEvent {
        MENU_BUTTON_DOUBLE_TAP
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                handleMenuButtonPress()
                true // Consume the event
            }
            else -> false // Don't consume other events
        }
    }

    private fun handleMenuButtonPress() {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - lastMenuButtonPressTime

        if (timeDiff < doubleTapThreshold) {
            // Double tap detected
            coroutineScope.launch {
                _buttonEvents.emit(ButtonEvent.MENU_BUTTON_DOUBLE_TAP)
            }
            lastMenuButtonPressTime = 0L // Reset to prevent triple-tap
        } else {
            // Single tap, store timestamp for potential double tap
            lastMenuButtonPressTime = currentTime

            // Auto-reset after threshold to prevent stale timestamps
            coroutineScope.launch {
                delay(doubleTapThreshold)
                if (abs(System.currentTimeMillis() - lastMenuButtonPressTime) >= doubleTapThreshold) {
                    lastMenuButtonPressTime = 0L
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        // Cleanup if needed
    }
}