package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.asta.calculatorapp.ui.viewmodel.CalculatorAction

@Composable
fun BasicKeypad(onAction: (CalculatorAction) -> Unit, modifier: Modifier = Modifier, keyHeight: Dp = 64.dp) {
    val rows = listOf(
        listOf("AC", "DEL", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("(", ")", "0", ".", "=")
    )
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { key ->
                    val type = when (key) {
                        "=" -> FuturisticButtonType.EQUALS
                        "AC", "DEL", "%" -> FuturisticButtonType.ACTION
                        "+", "-", "×", "÷" -> FuturisticButtonType.OPERATOR
                        else -> FuturisticButtonType.DIGIT
                    }
                    FuturisticButton(key, {
                        onAction(when (key) {
                            "AC" -> CalculatorAction.Clear
                            "DEL" -> CalculatorAction.Delete
                            "=" -> CalculatorAction.Calculate
                            "(" -> CalculatorAction.ParenthesisOpen
                            ")" -> CalculatorAction.ParenthesisClose
                            "+", "-", "×", "÷", "%" -> CalculatorAction.Operator(key)
                            else -> CalculatorAction.Digit(key)
                        })
                    }, Modifier.weight(1f).heightIn(min = keyHeight), type)
                }
            }
        }
    }
}
