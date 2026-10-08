package com.asta.calculatorapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.asta.calculatorapp.domain.evaluator.AngleUnit
import com.asta.calculatorapp.domain.evaluator.EvaluationResult
import com.asta.calculatorapp.domain.evaluator.ExpressionEvaluator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Digit -> handleDigit(action.digit)
            is CalculatorAction.Operator -> handleOperator(action.symbol)
            is CalculatorAction.Function -> handleFunction(action.fn)
            is CalculatorAction.Constant -> handleConstant(action.symbol)
            is CalculatorAction.ParenthesisOpen -> handleParenthesis("(")
            is CalculatorAction.ParenthesisClose -> handleParenthesis(")")
            is CalculatorAction.Factorial -> handleFactorial()
            is CalculatorAction.Clear -> handleClear()
            is CalculatorAction.ClearEntry -> handleClearEntry()
            is CalculatorAction.Delete -> handleDelete()
            is CalculatorAction.Calculate -> handleCalculate()
            is CalculatorAction.ToggleAngleUnit -> handleToggleAngleUnit()
            is CalculatorAction.ToggleScientific -> handleToggleScientific()
            is CalculatorAction.ToggleInverse -> handleToggleInverse()
            is CalculatorAction.ToggleSound -> handleToggleSound()
            is CalculatorAction.DismissError -> handleDismissError()
            is CalculatorAction.MemoryClear -> handleMemoryClear()
            is CalculatorAction.MemoryRecall -> handleMemoryRecall()
            is CalculatorAction.MemoryAdd -> handleMemoryAdd()
            is CalculatorAction.MemorySubtract -> handleMemorySubtract()
            is CalculatorAction.SelectHistoryItem -> handleRecallHistoryResult(action.item)
            is CalculatorAction.RecallHistoryExpression -> handleRecallHistoryExpression(action.item)
            is CalculatorAction.RecallHistoryResult -> handleRecallHistoryResult(action.item)
            is CalculatorAction.ClearHistory -> handleClearHistory()
        }
    }

    private fun handleDigit(digit: String) {
        _uiState.update { state ->
            val isNewStart = state.isErrorState || state.result.isNotEmpty()
            var currentExpr = if (isNewStart) "" else state.expression

            if (digit == ".") {
                // Prevent multiple decimals in the last number token
                val lastToken = currentExpr.split(" ", "(", ")", "×", "÷", "+", "-", "^", "%").lastOrNull() ?: ""
                if (lastToken.contains(".")) {
                    return@update state
                }
                if (currentExpr.isEmpty() || currentExpr.endsWith(" ") || currentExpr.endsWith("(")) {
                    currentExpr += "0."
                } else {
                    currentExpr += "."
                }
            } else {
                currentExpr += digit
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleOperator(symbol: String) {
        _uiState.update { state ->
            val normSymbol = when (symbol) {
                "*" -> "×"
                "/" -> "÷"
                else -> symbol
            }

            var currentExpr = if (state.result.isNotEmpty()) {
                state.result
            } else {
                state.expression
            }

            if (currentExpr.isEmpty()) {
                if (normSymbol == "-") {
                    currentExpr = "-"
                } else {
                    return@update state
                }
            } else if (endsWithOperator(currentExpr)) {
                // Replace last operator
                currentExpr = replaceLastOperator(currentExpr, normSymbol)
            } else {
                currentExpr += " $normSymbol "
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleFunction(fn: String) {
        _uiState.update { state ->
            val formattedFn = when (fn) {
                "sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "abs" -> "$fn("
                "sqrt", "√" -> "√("
                "sin⁻¹" -> "asin("
                "cos⁻¹" -> "acos("
                "tan⁻¹" -> "atan("
                else -> if (fn.endsWith("(")) fn else "$fn("
            }

            val currentExpr = if (state.result.isNotEmpty() || state.isErrorState) {
                formattedFn
            } else {
                state.expression + formattedFn
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleConstant(symbol: String) {
        _uiState.update { state ->
            val currentExpr = if (state.result.isNotEmpty() || state.isErrorState) {
                symbol
            } else {
                state.expression + symbol
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleParenthesis(p: String) {
        _uiState.update { state ->
            val currentExpr = if (state.result.isNotEmpty() || state.isErrorState) {
                p
            } else {
                state.expression + p
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleFactorial() {
        _uiState.update { state ->
            if (state.expression.isEmpty() && state.result.isEmpty()) return@update state
            val currentExpr = if (state.result.isNotEmpty()) {
                state.result + "!"
            } else {
                state.expression + "!"
            }

            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleClear() {
        _uiState.update { state ->
            state.copy(
                expression = "",
                previewResult = "",
                result = "",
                errorMessage = null,
                isErrorState = false
            )
        }
    }

    private fun handleClearEntry() {
        _uiState.update { state ->
            state.copy(
                expression = "",
                previewResult = ""
            )
        }
    }

    private fun handleDelete() {
        _uiState.update { state ->
            if (state.isErrorState) {
                return@update state.copy(
                    isErrorState = false,
                    errorMessage = null
                )
            }

            var expr = state.expression
            if (expr.isEmpty()) return@update state

            // Multi-char deletion (operators with spaces, function prefixes)
            val functionsToTrim = listOf(
                "asin(", "acos(", "atan(", "sin⁻¹(", "cos⁻¹(", "tan⁻¹(",
                "sin(", "cos(", "tan(", "log(", "ln(", "abs(", "√("
            )

            var trimmed = false
            for (fn in functionsToTrim) {
                if (expr.endsWith(fn)) {
                    expr = expr.substring(0, expr.length - fn.length)
                    trimmed = true
                    break
                }
            }

            if (!trimmed) {
                val operatorsToTrim = listOf(" + ", " - ", " × ", " ÷ ", " % ", " ^ ")
                for (op in operatorsToTrim) {
                    if (expr.endsWith(op)) {
                        expr = expr.substring(0, expr.length - op.length)
                        trimmed = true
                        break
                    }
                }
            }

            if (!trimmed) {
                expr = expr.dropLast(1)
            }

            val preview = evaluatePreview(expr, state.angleUnit)
            state.copy(
                expression = expr,
                previewResult = preview,
                result = ""
            )
        }
    }

    private fun handleCalculate() {
        _uiState.update { state ->
            val exprToEval = if (state.expression.isEmpty() && state.result.isNotEmpty()) {
                state.result
            } else {
                state.expression
            }

            if (exprToEval.isEmpty()) return@update state

            when (val evalResult = ExpressionEvaluator.evaluate(exprToEval, state.angleUnit)) {
                is EvaluationResult.Success -> {
                    val newItem = CalculationHistoryItem(
                        expression = exprToEval,
                        result = evalResult.formattedResult
                    )
                    state.copy(
                        result = evalResult.formattedResult,
                        previewResult = "",
                        history = listOf(newItem) + state.history,
                        isErrorState = false,
                        errorMessage = null
                    )
                }
                is EvaluationResult.Error -> {
                    state.copy(
                        isErrorState = true,
                        errorMessage = evalResult.message,
                        previewResult = ""
                    )
                }
            }
        }
    }

    private fun handleToggleAngleUnit() {
        _uiState.update { state ->
            val newUnit = if (state.angleUnit == AngleUnit.DEGREE) AngleUnit.RADIAN else AngleUnit.DEGREE
            val preview = evaluatePreview(state.expression, newUnit)
            state.copy(
                angleUnit = newUnit,
                previewResult = preview
            )
        }
    }

    private fun handleToggleScientific() {
        _uiState.update { state ->
            state.copy(isScientificExpanded = !state.isScientificExpanded)
        }
    }

    private fun handleToggleInverse() {
        _uiState.update { state ->
            state.copy(isInverse = !state.isInverse)
        }
    }

    private fun handleDismissError() {
        _uiState.update { state ->
            state.copy(errorMessage = null, isErrorState = false)
        }
    }

    private fun handleMemoryClear() {
        _uiState.update { state ->
            state.copy(memoryValue = 0.0, hasMemory = false)
        }
    }

    private fun handleMemoryRecall() {
        _uiState.update { state ->
            if (!state.hasMemory) return@update state
            val formattedMem = ExpressionEvaluator.formatResult(state.memoryValue)
            val currentExpr = if (state.result.isNotEmpty() || state.isErrorState) {
                formattedMem
            } else {
                state.expression + formattedMem
            }
            val preview = evaluatePreview(currentExpr, state.angleUnit)
            state.copy(
                expression = currentExpr,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleMemoryAdd() {
        _uiState.update { state ->
            val valToAdd = getCurrentValueForMemory(state) ?: return@update state
            val newMem = state.memoryValue + valToAdd
            state.copy(memoryValue = newMem, hasMemory = true)
        }
    }

    private fun handleMemorySubtract() {
        _uiState.update { state ->
            val valToSub = getCurrentValueForMemory(state) ?: return@update state
            val newMem = state.memoryValue - valToSub
            state.copy(memoryValue = newMem, hasMemory = true)
        }
    }

    private fun handleToggleSound() {
        _uiState.update { state ->
            state.copy(isSoundEnabled = !state.isSoundEnabled)
        }
    }

    private fun handleRecallHistoryExpression(item: CalculationHistoryItem) {
        _uiState.update { state ->
            val preview = evaluatePreview(item.expression, state.angleUnit)
            state.copy(
                expression = item.expression,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleRecallHistoryResult(item: CalculationHistoryItem) {
        _uiState.update { state ->
            val preview = evaluatePreview(item.result, state.angleUnit)
            state.copy(
                expression = item.result,
                previewResult = preview,
                result = "",
                isErrorState = false,
                errorMessage = null
            )
        }
    }

    private fun handleClearHistory() {
        _uiState.update { state ->
            state.copy(history = emptyList())
        }
    }

    private fun evaluatePreview(expr: String, angleUnit: AngleUnit): String {
        if (expr.trim().isEmpty()) return ""
        return when (val res = ExpressionEvaluator.evaluate(expr, angleUnit)) {
            is EvaluationResult.Success -> res.formattedResult
            else -> ""
        }
    }

    private fun getCurrentValueForMemory(state: CalculatorUiState): Double? {
        if (state.result.isNotEmpty()) {
            return state.result.toDoubleOrNull()
        }
        if (state.expression.isNotEmpty()) {
            return when (val eval = ExpressionEvaluator.evaluate(state.expression, state.angleUnit)) {
                is EvaluationResult.Success -> eval.value
                else -> null
            }
        }
        return null
    }

    private fun endsWithOperator(expr: String): Boolean {
        val trimmed = expr.trimEnd()
        return trimmed.endsWith("+") || trimmed.endsWith("-") || trimmed.endsWith("×") ||
                trimmed.endsWith("÷") || trimmed.endsWith("%") || trimmed.endsWith("^")
    }

    private fun replaceLastOperator(expr: String, newOp: String): String {
        val trimmed = expr.trimEnd()
        val ops = listOf("+", "-", "×", "÷", "%", "^")
        for (op in ops) {
            if (trimmed.endsWith(op)) {
                val base = trimmed.substring(0, trimmed.length - op.length).trimEnd()
                return "$base $newOp "
            }
        }
        return "$expr $newOp "
    }
}
