package com.asta.calculatorapp.ui.feedback

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

class SoundManager(context: Context) {
    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 35)
    } catch (e: Exception) {
        Log.w("SoundManager", "ToneGenerator failed to initialize", e)
        null
    }

    fun playClickSound(enabled: Boolean) {
        if (!enabled) return
        try {
            // First attempt to play system touch key click sound effect
            audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.5f)
                ?: toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 15)
        } catch (e: Exception) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 15)
            } catch (e2: Exception) {
                Log.w("SoundManager", "Error playing click sound", e2)
            }
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            Log.w("SoundManager", "Error releasing ToneGenerator", e)
        }
    }
}
