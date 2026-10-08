package com.asta.calculatorapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asta.calculatorapp.domain.evaluator.AngleUnit
import com.asta.calculatorapp.ui.components.BasicKeypad
import com.asta.calculatorapp.ui.components.DisplayPanel
import com.asta.calculatorapp.ui.components.EmbeddedHistoryPanel
import com.asta.calculatorapp.ui.components.HistorySheet
import com.asta.calculatorapp.ui.components.ScientificKeypad
import com.asta.calculatorapp.ui.feedback.rememberFeedbackHelper
import com.asta.calculatorapp.ui.theme.CalculatorAppTheme
import com.asta.calculatorapp.ui.theme.CherryContainerDeep
import com.asta.calculatorapp.ui.theme.DarkBgPrimary
import com.asta.calculatorapp.ui.theme.NeonCherryPrimary
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.SciFiGlassBorder
import com.asta.calculatorapp.ui.theme.SciFiGlassPanel
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextDim
import com.asta.calculatorapp.ui.theme.TextMuted
import com.asta.calculatorapp.ui.viewmodel.CalculatorAction
import com.asta.calculatorapp.ui.viewmodel.CalculatorUiState
import com.asta.calculatorapp.ui.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val feedbackHelper = rememberFeedbackHelper()

    CalculatorContent(
        uiState = uiState,
        onAction = { action ->
            feedbackHelper.triggerFeedback(uiState.isSoundEnabled)
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorContent(
    uiState: CalculatorUiState,
    onAction: (CalculatorAction) -> Unit
) {
    var showHistorySheet by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideScreen = maxWidth >= 600.dp

        // Modal History Sheet for standard screens (< 600dp)
        if (!isWideScreen && showHistorySheet) {
            HistorySheet(
                history = uiState.history,
                onRecallExpression = { item ->
                    onAction(CalculatorAction.RecallHistoryExpression(item))
                },
                onRecallResult = { item ->
                    onAction(CalculatorAction.RecallHistoryResult(item))
                },
                onClearHistory = {
                    onAction(CalculatorAction.ClearHistory)
                },
                onDismiss = { showHistorySheet = false }
            )
        }

        Scaffold(
            containerColor = DarkBgPrimary,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .drawBehind {
                        // Atmospheric background gradient blobs (positioned safely below top header bar)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(NeonCherryPrimary.copy(alpha = 0.08f), Color.Transparent)
                            ),
                            radius = size.width * 0.35f,
                            center = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.52f)
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(NeonCherrySecondary.copy(alpha = 0.06f), Color.Transparent)
                            ),
                            radius = size.width * 0.40f,
                            center = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.72f)
                        )
                    }
            ) {
                if (isWideScreen) {
                    // Wide / Tablet Split 2-Pane Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Pane: Keypad & Display
                        Column(
                            modifier = Modifier
                                .weight(1.2f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            TopHeaderBar(
                                isScientific = uiState.isScientificExpanded,
                                isSoundEnabled = uiState.isSoundEnabled,
                                angleUnit = uiState.angleUnit,
                                onToggleScientific = { onAction(CalculatorAction.ToggleScientific) },
                                onToggleSound = { onAction(CalculatorAction.ToggleSound) },
                                onToggleAngleUnit = { onAction(CalculatorAction.ToggleAngleUnit) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            DisplayPanel(
                                expression = uiState.expression,
                                previewResult = uiState.previewResult,
                                result = uiState.result,
                                isScientific = uiState.isScientificExpanded,
                                isInverse = uiState.isInverse,
                                hasMemory = uiState.hasMemory,
                                isErrorState = uiState.isErrorState,
                                errorMessage = uiState.errorMessage,
                                onToggleHistory = { showHistorySheet = true },
                                onDismissError = { onAction(CalculatorAction.DismissError) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AnimatedVisibility(
                                    visible = uiState.isScientificExpanded,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    ScientificKeypad(
                                        isInverse = uiState.isInverse,
                                        hasMemory = uiState.hasMemory,
                                        onAction = onAction,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                BasicKeypad(
                                    onAction = onAction,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            FooterTagline()
                        }

                        // Right Pane: Embedded Side History Panel
                        EmbeddedHistoryPanel(
                            history = uiState.history,
                            onRecallExpression = { item ->
                                onAction(CalculatorAction.RecallHistoryExpression(item))
                            },
                            onRecallResult = { item ->
                                onAction(CalculatorAction.RecallHistoryResult(item))
                            },
                            onClearHistory = {
                                onAction(CalculatorAction.ClearHistory)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                } else {
                    // Standard Screen Layout (Compact Window)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TopHeaderBar(
                            isScientific = uiState.isScientificExpanded,
                            isSoundEnabled = uiState.isSoundEnabled,
                            angleUnit = uiState.angleUnit,
                            onToggleScientific = { onAction(CalculatorAction.ToggleScientific) },
                            onToggleSound = { onAction(CalculatorAction.ToggleSound) },
                            onToggleAngleUnit = { onAction(CalculatorAction.ToggleAngleUnit) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DisplayPanel(
                            expression = uiState.expression,
                            previewResult = uiState.previewResult,
                            result = uiState.result,
                            isScientific = uiState.isScientificExpanded,
                            isInverse = uiState.isInverse,
                            hasMemory = uiState.hasMemory,
                            isErrorState = uiState.isErrorState,
                            errorMessage = uiState.errorMessage,
                            onToggleHistory = { showHistorySheet = true },
                            onDismissError = { onAction(CalculatorAction.DismissError) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AnimatedVisibility(
                                visible = uiState.isScientificExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                ScientificKeypad(
                                    isInverse = uiState.isInverse,
                                    hasMemory = uiState.hasMemory,
                                    onAction = onAction,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            BasicKeypad(
                                onAction = onAction,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FooterTagline()
                    }
                }
            }
        }
    }
}

@Composable
private fun TopHeaderBar(
    isScientific: Boolean,
    isSoundEnabled: Boolean,
    angleUnit: AngleUnit,
    onToggleScientific: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleAngleUnit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Branding
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(NeonCherryPrimary, NeonCherrySecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = TextBright,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "CHERRY CALC",
                    color = TextBright,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Asta's project",
                    color = NeonCherrySecondary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.2.sp
                )
            }
        }

        // Header Actions: Scientific Mode, Angle Unit (DEG/RAD), & Sound Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Scientific Mode Toggle Button
            HeaderPillButton(
                icon = Icons.Default.Functions,
                label = "SCI",
                isActive = isScientific,
                onClick = onToggleScientific
            )

            // Angle Unit Toggle Button (DEG / RAD)
            HeaderPillButton(
                icon = Icons.Default.Autorenew,
                label = if (angleUnit == AngleUnit.DEGREE) "DEG" else "RAD",
                isActive = true,
                onClick = onToggleAngleUnit
            )

            // Sound Toggle Button
            IconButton(
                onClick = onToggleSound,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isSoundEnabled) CherryContainerDeep else SciFiGlassPanel)
                    .border(1.dp, if (isSoundEnabled) NeonCherryPrimary else SciFiGlassBorder, CircleShape)
            ) {
                Icon(
                    imageVector = if (isSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = if (isSoundEnabled) "Sound Enabled" else "Sound Muted",
                    tint = if (isSoundEnabled) NeonCherrySecondary else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun HeaderPillButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) CherryContainerDeep else SciFiGlassPanel)
            .border(
                width = 1.dp,
                color = if (isActive) NeonCherryPrimary else SciFiGlassBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) NeonCherrySecondary else TextMuted,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            color = if (isActive) TextBright else TextMuted,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FooterTagline() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Cherry Calculator // Asta v1.0",
            color = TextDim,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.5.sp
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CalculatorScreenPreview() {
    CalculatorAppTheme {
        CalculatorContent(
            uiState = CalculatorUiState(
                expression = "sin(45) + 12.5 × 8",
                previewResult = "100.7071",
                angleUnit = AngleUnit.DEGREE,
                isScientificExpanded = true,
                hasMemory = true
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun CalculatorScreenTabletPreview() {
    CalculatorAppTheme {
        CalculatorContent(
            uiState = CalculatorUiState(
                expression = "sin(45) + 12.5 × 8",
                previewResult = "100.7071",
                angleUnit = AngleUnit.DEGREE,
                isScientificExpanded = true,
                hasMemory = true
            ),
            onAction = {}
        )
    }
}
