package com.asta.calculatorapp.ui.viewmodel

import com.asta.calculatorapp.domain.evaluator.AngleUnit

data class CalculatorUiState(
    val expression: String = "",
    val previewResult: String = "",
    val result: String = "",
    val angleUnit: AngleUnit = AngleUnit.DEGREE,
    val isScientificExpanded: Boolean = false,
    val isInverse: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val memoryValue: Double = 0.0,
    val hasMemory: Boolean = false,
    val history: List<CalculationHistoryItem> = emptyList(),
    val errorMessage: String? = null,
    val isErrorState: Boolean = false
)
