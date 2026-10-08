package com.asta.calculatorapp.ui.viewmodel

import com.asta.calculatorapp.domain.evaluator.AngleUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun `test initial state`() {
        val state = viewModel.uiState.value
        assertEquals("", state.expression)
        assertEquals("", state.result)
        assertEquals("", state.previewResult)
        assertEquals(AngleUnit.DEGREE, state.angleUnit)
        assertFalse(state.isScientificExpanded)
        assertFalse(state.isInverse)
        assertTrue(state.isSoundEnabled)
        assertFalse(state.hasMemory)
        assertEquals(0.0, state.memoryValue, 1e-9)
        assertTrue(state.history.isEmpty())
        assertNull(state.errorMessage)
        assertFalse(state.isErrorState)
    }

    @Test
    fun `test toggle sound`() {
        assertTrue(viewModel.uiState.value.isSoundEnabled)
        viewModel.onAction(CalculatorAction.ToggleSound)
        assertFalse(viewModel.uiState.value.isSoundEnabled)
        viewModel.onAction(CalculatorAction.ToggleSound)
        assertTrue(viewModel.uiState.value.isSoundEnabled)
    }

    @Test
    fun `test digit input and live preview`() {
        viewModel.onAction(CalculatorAction.Digit("1"))
        viewModel.onAction(CalculatorAction.Digit("2"))
        viewModel.onAction(CalculatorAction.Digit("."))
        viewModel.onAction(CalculatorAction.Digit("5"))

        val state = viewModel.uiState.value
        assertEquals("12.5", state.expression)
        assertEquals("12.5", state.previewResult)
    }

    @Test
    fun `test operator input and evaluation`() {
        viewModel.onAction(CalculatorAction.Digit("1"))
        viewModel.onAction(CalculatorAction.Digit("0"))
        viewModel.onAction(CalculatorAction.Operator("+"))
        viewModel.onAction(CalculatorAction.Digit("5"))

        var state = viewModel.uiState.value
        assertEquals("10 + 5", state.expression)
        assertEquals("15", state.previewResult)

        viewModel.onAction(CalculatorAction.Calculate)
        state = viewModel.uiState.value
        assertEquals("15", state.result)
        assertEquals(1, state.history.size)
        assertEquals("10 + 5", state.history[0].expression)
        assertEquals("15", state.history[0].result)
    }

    @Test
    fun `test chaining operator after calculate`() {
        viewModel.onAction(CalculatorAction.Digit("8"))
        viewModel.onAction(CalculatorAction.Calculate)
        viewModel.onAction(CalculatorAction.Operator("×"))
        viewModel.onAction(CalculatorAction.Digit("2"))

        var state = viewModel.uiState.value
        assertEquals("8 × 2", state.expression)

        viewModel.onAction(CalculatorAction.Calculate)
        state = viewModel.uiState.value
        assertEquals("16", state.result)
    }

    @Test
    fun `test function and angle mode toggle`() {
        viewModel.onAction(CalculatorAction.Function("sin"))
        viewModel.onAction(CalculatorAction.Digit("3"))
        viewModel.onAction(CalculatorAction.Digit("0"))
        viewModel.onAction(CalculatorAction.ParenthesisClose)

        var state = viewModel.uiState.value
        assertEquals("sin(30)", state.expression)
        assertEquals("0.5", state.previewResult)

        viewModel.onAction(CalculatorAction.ToggleAngleUnit)
        state = viewModel.uiState.value
        assertEquals(AngleUnit.RADIAN, state.angleUnit)
        // In radians, sin(30) preview will re-evaluate
        assertFalse(state.previewResult.isEmpty())
    }

    @Test
    fun `test error handling - division by zero`() {
        viewModel.onAction(CalculatorAction.Digit("5"))
        viewModel.onAction(CalculatorAction.Operator("÷"))
        viewModel.onAction(CalculatorAction.Digit("0"))
        viewModel.onAction(CalculatorAction.Calculate)

        val state = viewModel.uiState.value
        assertTrue(state.isErrorState)
        assertEquals("Cannot divide by zero", state.errorMessage)
    }

    @Test
    fun `test delete action`() {
        viewModel.onAction(CalculatorAction.Function("sin"))
        viewModel.onAction(CalculatorAction.Digit("3"))
        viewModel.onAction(CalculatorAction.Digit("0"))

        var state = viewModel.uiState.value
        assertEquals("sin(30", state.expression)

        viewModel.onAction(CalculatorAction.Delete)
        state = viewModel.uiState.value
        assertEquals("sin(3", state.expression)

        viewModel.onAction(CalculatorAction.Delete)
        state = viewModel.uiState.value
        assertEquals("sin(", state.expression)

        viewModel.onAction(CalculatorAction.Delete)
        state = viewModel.uiState.value
        assertEquals("", state.expression)
    }

    @Test
    fun `test memory operations`() {
        viewModel.onAction(CalculatorAction.Digit("5"))
        viewModel.onAction(CalculatorAction.Calculate)
        viewModel.onAction(CalculatorAction.MemoryAdd)

        var state = viewModel.uiState.value
        assertTrue(state.hasMemory)
        assertEquals(5.0, state.memoryValue, 1e-9)

        viewModel.onAction(CalculatorAction.Clear)
        viewModel.onAction(CalculatorAction.MemoryRecall)
        state = viewModel.uiState.value
        assertEquals("5", state.expression)

        viewModel.onAction(CalculatorAction.MemoryClear)
        state = viewModel.uiState.value
        assertFalse(state.hasMemory)
        assertEquals(0.0, state.memoryValue, 1e-9)
    }

    @Test
    fun `test recall history expression and result`() {
        viewModel.onAction(CalculatorAction.Digit("4"))
        viewModel.onAction(CalculatorAction.Operator("×"))
        viewModel.onAction(CalculatorAction.Digit("5"))
        viewModel.onAction(CalculatorAction.Calculate)

        val historyItem = viewModel.uiState.value.history[0]
        viewModel.onAction(CalculatorAction.Clear)

        // Recall Result
        viewModel.onAction(CalculatorAction.RecallHistoryResult(historyItem))
        var state = viewModel.uiState.value
        assertEquals("20", state.expression)

        viewModel.onAction(CalculatorAction.Clear)

        // Recall Expression
        viewModel.onAction(CalculatorAction.RecallHistoryExpression(historyItem))
        state = viewModel.uiState.value
        assertEquals("4 × 5", state.expression)
    }

    @Test
    fun `test clear history`() {
        viewModel.onAction(CalculatorAction.Digit("4"))
        viewModel.onAction(CalculatorAction.Operator("+"))
        viewModel.onAction(CalculatorAction.Digit("2"))
        viewModel.onAction(CalculatorAction.Calculate)

        assertEquals(1, viewModel.uiState.value.history.size)

        viewModel.onAction(CalculatorAction.ClearHistory)
        assertTrue(viewModel.uiState.value.history.isEmpty())
    }
}
