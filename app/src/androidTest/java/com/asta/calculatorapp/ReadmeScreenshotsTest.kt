package com.asta.calculatorapp

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.asta.calculatorapp.ui.screens.CalculatorContent
import com.asta.calculatorapp.ui.theme.Appearance
import com.asta.calculatorapp.ui.theme.CalculatorAppTheme
import com.asta.calculatorapp.ui.viewmodel.CalculationHistoryItem
import com.asta.calculatorapp.ui.viewmodel.CalculatorUiState
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Captures production composables with temporary demo state; no user preferences or history are changed. */
@RunWith(AndroidJUnit4::class)
class ReadmeScreenshotsTest {
    @get:Rule val compose = createComposeRule()

    @Test fun captureLightGallery() {
        val now = System.currentTimeMillis()
        val history = listOf(
            CalculationHistoryItem(expression = "200 + 10%", result = "220", timestamp = now),
            CalculationHistoryItem(expression = "sin(30)", result = "0.5", timestamp = now - 60000),
            CalculationHistoryItem(expression = "48 × 12", result = "576", timestamp = now - 120000)
        )
        val state = mutableStateOf(CalculatorUiState(expression = "200 + 10%", result = "220", history = history))
        compose.setContent {
            CalculatorAppTheme(darkTheme = false) {
                CalculatorContent(state.value, Appearance.LIGHT, onAction = {})
            }
        }
        save("calculator-light.png")
        compose.runOnIdle {
            state.value = state.value.copy(expression = "sin(30)", result = "", previewResult = "0.5", isScientificExpanded = true)
        }
        save("scientific-light.png")
        compose.onNodeWithContentDescription("Calculation history").performClick()
        compose.mainClock.advanceTimeBy(1000)
        compose.onNodeWithText("History").assertIsDisplayed()
        save("history-light.png", dialog = true)
    }

    private fun save(name: String, dialog: Boolean = false) {
        compose.waitForIdle()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val destination = File(context.getExternalFilesDir(null), "readme-screenshots").apply { mkdirs() }
        File(destination, name).outputStream().use {
            val node = if (dialog) compose.onNode(isDialog()) else compose.onAllNodes(isRoot()).onLast()
            node.captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
