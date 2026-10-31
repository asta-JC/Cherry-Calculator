package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Alignment
import com.asta.calculatorapp.domain.evaluator.AngleUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asta.calculatorapp.ui.viewmodel.CalculatorAction

/**
 * ScientificKeypad:
 * Optional scientific panel containing trigonometry, functions, and memory operations for Cherry Calc.
 */
@Composable
fun ScientificKeypad(
    isInverse: Boolean,
    hasMemory: Boolean,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier,
    angleUnit: AngleUnit = AngleUnit.DEGREE
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Angle unit", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            InputChip(selected = true,
                onClick = { onAction(CalculatorAction.ToggleAngleUnit) },
                modifier = Modifier.heightIn(min = 48.dp),
                label = { Text(if (angleUnit == AngleUnit.DEGREE) "Degrees" else "Radians") },
                leadingIcon = { Icon(Icons.Outlined.SquareFoot, null, Modifier.size(18.dp)) })
        }
        // --- ROW 1: 2nd, MC, MR, M+, M- ---

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "2nd",
                onClick = { onAction(CalculatorAction.ToggleInverse) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                isHighlighted = isInverse,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "MC",
                onClick = { onAction(CalculatorAction.MemoryClear) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "MR",
                onClick = { onAction(CalculatorAction.MemoryRecall) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                isHighlighted = hasMemory,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "M+",
                onClick = { onAction(CalculatorAction.MemoryAdd) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "M-",
                onClick = { onAction(CalculatorAction.MemorySubtract) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
        }

        // --- ROW 2: Trig Functions & Constants ---

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = if (isInverse) "sin⁻¹" else "sin",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "asin" else "sin"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = if (isInverse) "cos⁻¹" else "cos",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "acos" else "cos"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = if (isInverse) "tan⁻¹" else "tan",
                onClick = {
                    onAction(CalculatorAction.Function(if (isInverse) "atan" else "tan"))
                },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "π",
                onClick = { onAction(CalculatorAction.Constant("π")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "e",
                onClick = { onAction(CalculatorAction.Constant("e")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
        }

        // --- ROW 3: Powers & Logarithms ---

        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "xⁿ",
                onClick = { onAction(CalculatorAction.Operator("^")) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
            FuturisticButton(
                text = "x!",
                onClick = { onAction(CalculatorAction.Factorial) },
                buttonType = FuturisticButtonType.SCIENTIFIC,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
