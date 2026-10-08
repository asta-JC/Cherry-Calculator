package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.asta.calculatorapp.ui.viewmodel.CalculatorAction

@Composable
fun ScientificKeypad(
    isInverse: Boolean,
    hasMemory: Boolean,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { clip = false },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: Memory Operations + INV Toggle
        val pressedMap1 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow1Active = isInverse || hasMemory || pressedMap1.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow1Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "2nd",
                onClick = { onAction(CalculatorAction.ToggleInverse) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                isHighlighted = isInverse,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[0] = pressed }
            )
            FuturisticButton(
                text = "MC",
                onClick = { onAction(CalculatorAction.MemoryClear) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[1] = pressed }
            )
            FuturisticButton(
                text = "MR",
                onClick = { onAction(CalculatorAction.MemoryRecall) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                isHighlighted = hasMemory,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[2] = pressed }
            )
            FuturisticButton(
                text = "M+",
                onClick = { onAction(CalculatorAction.MemoryAdd) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[3] = pressed }
            )
            FuturisticButton(
                text = "M-",
                onClick = { onAction(CalculatorAction.MemorySubtract) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[4] = pressed }
            )
        }

        // Row 2: Trig functions & Constants
        val pressedMap2 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow2Active = pressedMap2.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow2Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = if (isInverse) "sin⁻¹" else "sin",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "asin" else "sin"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[0] = pressed }
            )
            FuturisticButton(
                text = if (isInverse) "cos⁻¹" else "cos",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "acos" else "cos"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[1] = pressed }
            )
            FuturisticButton(
                text = if (isInverse) "tan⁻¹" else "tan",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "atan" else "tan"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[2] = pressed }
            )
            FuturisticButton(
                text = "π",
                onClick = { onAction(CalculatorAction.Constant("π")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[3] = pressed }
            )
            FuturisticButton(
                text = "e",
                onClick = { onAction(CalculatorAction.Constant("e")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[4] = pressed }
            )
        }

        // Row 3: Powers, Logs & Functions
        val pressedMap3 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow3Active = pressedMap3.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow3Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = if (isInverse) "eⁿ" else "ln",
                onClick = {
                    if (isInverse) {
                        onAction(CalculatorAction.Constant("e"))
                        onAction(CalculatorAction.Operator("^"))
                    } else {
                        onAction(CalculatorAction.Function("ln"))
                    }
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[0] = pressed }
            )
            FuturisticButton(
                text = if (isInverse) "10ⁿ" else "log",
                onClick = {
                    if (isInverse) {
                        onAction(CalculatorAction.Digit("10"))
                        onAction(CalculatorAction.Operator("^"))
                    } else {
                        onAction(CalculatorAction.Function("log"))
                    }
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[1] = pressed }
            )
            FuturisticButton(
                text = if (isInverse) "x²" else "√",
                onClick = {
                    if (isInverse) {
                        onAction(CalculatorAction.Operator("^"))
                        onAction(CalculatorAction.Digit("2"))
                    } else {
                        onAction(CalculatorAction.Function("√"))
                    }
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[2] = pressed }
            )
            FuturisticButton(
                text = "xⁿ",
                onClick = { onAction(CalculatorAction.Operator("^")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[3] = pressed }
            )
            FuturisticButton(
                text = "x!",
                onClick = { onAction(CalculatorAction.Factorial) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[4] = pressed }
            )
        }
    }
}
