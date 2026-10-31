package com.asta.calculatorapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import com.asta.calculatorapp.ui.theme.Appearance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asta.calculatorapp.R
import com.asta.calculatorapp.ui.components.*
import com.asta.calculatorapp.ui.feedback.rememberFeedbackHelper
import com.asta.calculatorapp.ui.viewmodel.*

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel(),
    appearance: Appearance = Appearance.SYSTEM, onAppearanceChange: (Appearance) -> Unit = {}) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val feedback = rememberFeedbackHelper()
    CalculatorContent(state, appearance, onAppearanceChange) {
        feedback.triggerFeedback(state.isSoundEnabled)
        viewModel.onAction(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorContent(uiState: CalculatorUiState, appearance: Appearance = Appearance.SYSTEM,
    onAppearanceChange: (Appearance) -> Unit = {}, onAction: (CalculatorAction) -> Unit) {
    var historyOpen by rememberSaveable { mutableStateOf(false) }
    if (historyOpen) HistorySheet(uiState.history,
        { onAction(CalculatorAction.RecallHistoryExpression(it)) },
        { onAction(CalculatorAction.RecallHistoryResult(it)) },
        { onAction(CalculatorAction.ClearHistory) }, { historyOpen = false })

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { insets ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(insets)) {
            val wide = maxWidth >= 700.dp
            // Give the keypad the remaining space on tall screens, and scroll on compact screens.
            val keyHeight = ((maxHeight - 430.dp) / 5).coerceIn(60.dp, 96.dp)
            Row(Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(if (MaterialTheme.colorScheme.background.luminance() < 0.5f)
                            R.drawable.cherryapps_logo_full_dark else R.drawable.cherryapps_logo_full),
                            null, Modifier.size(32.dp).padding(2.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Cherry Calculator", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                        IconButton(onClick = { historyOpen = true }) {
                            Icon(Icons.Default.History, "Calculation history",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                        AppearanceControl(appearance, onAppearanceChange)
                    }
                    DisplayPanel(uiState.expression, uiState.previewResult, uiState.result,
                        uiState.isScientificExpanded, uiState.isInverse, uiState.hasMemory,
                        uiState.isErrorState, uiState.errorMessage,
                        { onAction(CalculatorAction.DismissError) })
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        FilterChip(selected = uiState.isScientificExpanded,
                            onClick = { onAction(CalculatorAction.ToggleScientific) },
                            label = { Text("Scientific") },
                            leadingIcon = { Icon(Icons.Default.Functions, null, Modifier.size(18.dp)) })
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { onAction(CalculatorAction.ToggleSound) }) {
                            Icon(if (uiState.isSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                if (uiState.isSoundEnabled) "Mute key sounds" else "Enable key sounds",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    AnimatedVisibility(uiState.isScientificExpanded,
                        enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
                        ScientificKeypad(uiState.isInverse, uiState.hasMemory, onAction, angleUnit = uiState.angleUnit)
                    }
                    BasicKeypad(onAction, keyHeight = keyHeight)
                    Spacer(Modifier.height(4.dp))
                }
                if (wide) EmbeddedHistoryPanel(uiState.history,
                    { onAction(CalculatorAction.RecallHistoryExpression(it)) },
                    { onAction(CalculatorAction.RecallHistoryResult(it)) },
                    { onAction(CalculatorAction.ClearHistory) },
                    modifier = Modifier.weight(0.85f).fillMaxHeight().padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun AppearanceControl(appearance: Appearance, onChange: (Appearance) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val icon = when (appearance) {
        Appearance.SYSTEM -> Icons.Outlined.BrightnessAuto
        Appearance.LIGHT -> Icons.Outlined.LightMode
        Appearance.DARK -> Icons.Outlined.DarkMode
    }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(icon, "Appearance: ${appearance.name.lowercase()}. Change light or dark theme",
                tint = MaterialTheme.colorScheme.primary)
        }
        DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            Appearance.entries.forEach { mode ->
                DropdownMenuItem(text = { Text(when (mode) {
                    Appearance.SYSTEM -> "System appearance"
                    Appearance.LIGHT -> "Light"
                    Appearance.DARK -> "Dark"
                }) }, onClick = { onChange(mode); expanded = false },
                    leadingIcon = { Icon(when (mode) {
                        Appearance.SYSTEM -> Icons.Outlined.BrightnessAuto
                        Appearance.LIGHT -> Icons.Outlined.LightMode
                        Appearance.DARK -> Icons.Outlined.DarkMode
                    }, null) },
                    trailingIcon = { if (appearance == mode) Icon(Icons.Default.Check, "Selected") })
            }
        }
    }
}
