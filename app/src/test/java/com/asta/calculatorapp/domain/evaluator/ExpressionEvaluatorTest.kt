package com.asta.calculatorapp.domain.evaluator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpressionEvaluatorTest {

    @Test
    fun `test basic arithmetic operations`() {
        val result1 = ExpressionEvaluator.evaluate("2 + 3")
        assertTrue(result1 is EvaluationResult.Success)
        assertEquals("5", (result1 as EvaluationResult.Success).formattedResult)

        val result2 = ExpressionEvaluator.evaluate("10 - 4 * 2")
        assertTrue(result2 is EvaluationResult.Success)
        assertEquals("2", (result2 as EvaluationResult.Success).formattedResult)

        val result3 = ExpressionEvaluator.evaluate("15 / 3")
        assertTrue(result3 is EvaluationResult.Success)
        assertEquals("5", (result3 as EvaluationResult.Success).formattedResult)

        val result4 = ExpressionEvaluator.evaluate("10 % 3")
        assertTrue(result4 is EvaluationResult.Success)
        assertEquals("1", (result4 as EvaluationResult.Success).formattedResult)
    }

    @Test
    fun `test scientific functions in degrees`() {
        val sin30 = ExpressionEvaluator.evaluate("sin(30)", AngleUnit.DEGREE)
        assertTrue(sin30 is EvaluationResult.Success)
        assertEquals("0.5", (sin30 as EvaluationResult.Success).formattedResult)

        val cos60 = ExpressionEvaluator.evaluate("cos(60)", AngleUnit.DEGREE)
        assertTrue(cos60 is EvaluationResult.Success)
        assertEquals("0.5", (cos60 as EvaluationResult.Success).formattedResult)

        val tan45 = ExpressionEvaluator.evaluate("tan(45)", AngleUnit.DEGREE)
        assertTrue(tan45 is EvaluationResult.Success)
        assertEquals("1", (tan45 as EvaluationResult.Success).formattedResult)

        val sqrt16 = ExpressionEvaluator.evaluate("√(16)")
        assertTrue(sqrt16 is EvaluationResult.Success)
        assertEquals("4", (sqrt16 as EvaluationResult.Success).formattedResult)

        val log100 = ExpressionEvaluator.evaluate("log(100)")
        assertTrue(log100 is EvaluationResult.Success)
        assertEquals("2", (log100 as EvaluationResult.Success).formattedResult)
    }

    @Test
    fun `test trigonometric functions in radians`() {
        val sinPi = ExpressionEvaluator.evaluate("sin(π)", AngleUnit.RADIAN)
        assertTrue(sinPi is EvaluationResult.Success)
        assertEquals("0", (sinPi as EvaluationResult.Success).formattedResult)

        val cosPi = ExpressionEvaluator.evaluate("cos(π)", AngleUnit.RADIAN)
        assertTrue(cosPi is EvaluationResult.Success)
        assertEquals("-1", (cosPi as EvaluationResult.Success).formattedResult)
    }

    @Test
    fun `test powers and factorials`() {
        val pow2_3 = ExpressionEvaluator.evaluate("2 ^ 3")
        assertTrue(pow2_3 is EvaluationResult.Success)
        assertEquals("8", (pow2_3 as EvaluationResult.Success).formattedResult)

        val fact5 = ExpressionEvaluator.evaluate("5!")
        assertTrue(fact5 is EvaluationResult.Success)
        assertEquals("120", (fact5 as EvaluationResult.Success).formattedResult)
    }

    @Test
    fun `test implicit multiplication`() {
        val mul1 = ExpressionEvaluator.evaluate("2π")
        assertTrue(mul1 is EvaluationResult.Success)
        val val1 = (mul1 as EvaluationResult.Success).value
        assertEquals(2 * Math.PI, val1, 1e-9)

        val mul2 = ExpressionEvaluator.evaluate("3(4+5)")
        assertTrue(mul2 is EvaluationResult.Success)
        assertEquals("27", (mul2 as EvaluationResult.Success).formattedResult)

        val mul3 = ExpressionEvaluator.evaluate("2sin(30)", AngleUnit.DEGREE)
        assertTrue(mul3 is EvaluationResult.Success)
        assertEquals("1", (mul3 as EvaluationResult.Success).formattedResult)
    }

    @Test
    fun `test division by zero error`() {
        val res = ExpressionEvaluator.evaluate("10 / 0")
        assertTrue(res is EvaluationResult.Error.DivisionByZero)
    }

    @Test
    fun `test domain error`() {
        val resSqrt = ExpressionEvaluator.evaluate("sqrt(-4)")
        assertTrue(resSqrt is EvaluationResult.Error.DomainError)

        val resLog = ExpressionEvaluator.evaluate("log(-10)")
        assertTrue(resLog is EvaluationResult.Error.DomainError)

        val resTan90 = ExpressionEvaluator.evaluate("tan(90)", AngleUnit.DEGREE)
        assertTrue(resTan90 is EvaluationResult.Error.DomainError)
    }

    @Test
    fun `test syntax error`() {
        val res1 = ExpressionEvaluator.evaluate("5 + * 3")
        assertTrue(res1 is EvaluationResult.Error.SyntaxError)

        val res2 = ExpressionEvaluator.evaluate("(2 + 3")
        assertTrue(res2 is EvaluationResult.Error.SyntaxError)
    }

    @Test
    fun `test precision handling`() {
        val res = ExpressionEvaluator.evaluate("0.1 + 0.2")
        assertTrue(res is EvaluationResult.Success)
        assertEquals("0.3", (res as EvaluationResult.Success).formattedResult)
    }
}
