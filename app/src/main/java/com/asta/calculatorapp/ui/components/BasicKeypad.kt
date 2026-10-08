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
fun BasicKeypad(
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { clip = false },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: AC, DEL, %, ÷
        val pressedMap1 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow1Active = pressedMap1.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow1Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "AC",
                onClick = { onAction(CalculatorAction.Clear) },
                buttonType = FuturisticButtonType.ACTION,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[0] = pressed }
            )
            FuturisticButton(
                text = "DEL",
                onClick = { onAction(CalculatorAction.Delete) },
                buttonType = FuturisticButtonType.ACTION,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[1] = pressed }
            )
            FuturisticButton(
                text = "%",
                onClick = { onAction(CalculatorAction.Operator("%")) },
                buttonType = FuturisticButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[2] = pressed }
            )
            FuturisticButton(
                text = "÷",
                onClick = { onAction(CalculatorAction.Operator("÷")) },
                buttonType = FuturisticButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap1[3] = pressed }
            )
        }

        // Row 2: 7, 8, 9, ×
        val pressedMap2 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow2Active = pressedMap2.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow2Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "7",
                onClick = { onAction(CalculatorAction.Digit("7")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[0] = pressed }
            )
            FuturisticButton(
                text = "8",
                onClick = { onAction(CalculatorAction.Digit("8")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[1] = pressed }
            )
            FuturisticButton(
                text = "9",
                onClick = { onAction(CalculatorAction.Digit("9")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[2] = pressed }
            )
            FuturisticButton(
                text = "×",
                onClick = { onAction(CalculatorAction.Operator("×")) },
                buttonType = FuturisticButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap2[3] = pressed }
            )
        }

        // Row 3: 4, 5, 6, -
        val pressedMap3 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow3Active = pressedMap3.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow3Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "4",
                onClick = { onAction(CalculatorAction.Digit("4")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[0] = pressed }
            )
            FuturisticButton(
                text = "5",
                onClick = { onAction(CalculatorAction.Digit("5")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[1] = pressed }
            )
            FuturisticButton(
                text = "6",
                onClick = { onAction(CalculatorAction.Digit("6")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[2] = pressed }
            )
            FuturisticButton(
                text = "-",
                onClick = { onAction(CalculatorAction.Operator("-")) },
                buttonType = FuturisticButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap3[3] = pressed }
            )
        }

        // Row 4: 1, 2, 3, +
        val pressedMap4 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow4Active = pressedMap4.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow4Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "1",
                onClick = { onAction(CalculatorAction.Digit("1")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap4[0] = pressed }
            )
            FuturisticButton(
                text = "2",
                onClick = { onAction(CalculatorAction.Digit("2")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap4[1] = pressed }
            )
            FuturisticButton(
                text = "3",
                onClick = { onAction(CalculatorAction.Digit("3")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap4[2] = pressed }
            )
            FuturisticButton(
                text = "+",
                onClick = { onAction(CalculatorAction.Operator("+")) },
                buttonType = FuturisticButtonType.OPERATOR,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap4[3] = pressed }
            )
        }

        // Row 5: 0, ., ( ), =
        val pressedMap5 = remember { mutableStateMapOf<Int, Boolean>() }
        val isRow5Active = pressedMap5.values.any { it }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(if (isRow5Active) 10f else 0f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuturisticButton(
                text = "0",
                onClick = { onAction(CalculatorAction.Digit("0")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap5[0] = pressed }
            )
            FuturisticButton(
                text = ".",
                onClick = { onAction(CalculatorAction.Digit(".")) },
                buttonType = FuturisticButtonType.DIGIT,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap5[1] = pressed }
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FuturisticButton(
                    text = "(",
                    onClick = { onAction(CalculatorAction.ParenthesisOpen) },
                    buttonType = FuturisticButtonType.SCIENTIFIC,
                    modifier = Modifier.weight(1f),
                    onPressChanged = { pressed -> pressedMap5[2] = pressed }
                )
                FuturisticButton(
                    text = ")",
                    onClick = { onAction(CalculatorAction.ParenthesisClose) },
                    buttonType = FuturisticButtonType.SCIENTIFIC,
                    modifier = Modifier.weight(1f),
                    onPressChanged = { pressed -> pressedMap5[3] = pressed }
                )
            }
            FuturisticButton(
                text = "=",
                onClick = { onAction(CalculatorAction.Calculate) },
                buttonType = FuturisticButtonType.EQUALS,
                modifier = Modifier.weight(1f),
                onPressChanged = { pressed -> pressedMap5[4] = pressed }
            )
        }
    }
}
