package com.asta.calculatorapp.ui.viewmodel

sealed interface CalculatorAction {
    data class Digit(val digit: String) : CalculatorAction
    data class Operator(val symbol: String) : CalculatorAction
    data class Function(val fn: String) : CalculatorAction
    data class Constant(val symbol: String) : CalculatorAction
    object ParenthesisOpen : CalculatorAction
    object ParenthesisClose : CalculatorAction
    object Factorial : CalculatorAction
    object Clear : CalculatorAction
    object ClearEntry : CalculatorAction
    object Delete : CalculatorAction
    object Calculate : CalculatorAction
    object ToggleAngleUnit : CalculatorAction
    object ToggleScientific : CalculatorAction
    object ToggleInverse : CalculatorAction
    object ToggleSound : CalculatorAction
    object DismissError : CalculatorAction

    // Memory operations
    object MemoryClear : CalculatorAction
    object MemoryRecall : CalculatorAction
    object MemoryAdd : CalculatorAction
    object MemorySubtract : CalculatorAction

    // History operations
    data class SelectHistoryItem(val item: CalculationHistoryItem) : CalculatorAction
    data class RecallHistoryExpression(val item: CalculationHistoryItem) : CalculatorAction
    data class RecallHistoryResult(val item: CalculationHistoryItem) : CalculatorAction
    object ClearHistory : CalculatorAction
}
