package com.asta.calculatorapp.ui.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

class FeedbackHelper(
    private val soundManager: SoundManager,
    private val hapticFeedback: HapticFeedback?
) {
    fun triggerFeedback(isSoundEnabled: Boolean) {
        // Tactile haptic feedback for key tap
        try {
            hapticFeedback?.performHapticFeedback(HapticFeedbackType.KeyboardTap)
        } catch (_: Exception) {
            try {
                hapticFeedback?.performHapticFeedback(HapticFeedbackType.VirtualKey)
            } catch (_: Exception) {}
        }

        // Audio sound feedback
        soundManager.playClickSound(isSoundEnabled)
    }
}

@Composable
fun rememberFeedbackHelper(): FeedbackHelper {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val soundManager = remember(context) { SoundManager(context) }

    DisposableEffect(soundManager) {
        onDispose {
            soundManager.release()
        }
    }

    return remember(soundManager, hapticFeedback) {
        FeedbackHelper(soundManager, hapticFeedback)
    }
}
