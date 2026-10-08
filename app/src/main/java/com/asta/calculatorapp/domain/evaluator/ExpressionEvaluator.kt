package com.asta.calculatorapp.domain.evaluator

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object ExpressionEvaluator {

    fun evaluate(expression: String, angleUnit: AngleUnit = AngleUnit.DEGREE): EvaluationResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            return EvaluationResult.Success(0.0, "0")
        }

        return try {
            val tokens = tokenize(trimmed)
            if (tokens.isEmpty()) {
                return EvaluationResult.Success(0.0, "0")
            }
            val parser = Parser(tokens, angleUnit)
            val result = parser.parse()
            if (result.isNaN()) {
                EvaluationResult.Error.DomainError
            } else if (result.isInfinite()) {
                EvaluationResult.Error.DivisionByZero
            } else {
                val rounded = cleanupPrecision(result)
                val formatted = formatResult(rounded)
                EvaluationResult.Success(rounded, formatted)
            }
        } catch (e: EvaluationException) {
            when (e) {
                is EvaluationException.DivisionByZero -> EvaluationResult.Error.DivisionByZero
                is EvaluationException.DomainError -> EvaluationResult.Error.DomainError
                is EvaluationException.SyntaxError -> EvaluationResult.Error.SyntaxError
                is EvaluationException.CustomError -> EvaluationResult.Error.CustomError(e.msg)
            }
        } catch (_: Exception) {
            EvaluationResult.Error.SyntaxError
        }
    }

    private sealed class EvaluationException : RuntimeException() {
        data object DivisionByZero : EvaluationException()
        data object DomainError : EvaluationException()
        data object SyntaxError : EvaluationException()
        class CustomError(val msg: String) : EvaluationException()
    }

    private sealed interface Token {
        data class Number(val value: Double) : Token
        data class Identifier(val name: String) : Token
        data object Plus : Token
        data object Minus : Token
        data object Multiply : Token
        data object Divide : Token
        data object Modulo : Token
        data object Power : Token
        data object Factorial : Token
        data object LParen : Token
        data object RParen : Token
    }

    private fun tokenize(input: String): List<Token> {
        val rawTokens = mutableListOf<Token>()
        var i = 0
        val len = input.length

        while (i < len) {
            val c = input[i]
            when {
                c.isWhitespace() -> i++
                c == '+' -> { rawTokens.add(Token.Plus); i++ }
                c == '-' -> { rawTokens.add(Token.Minus); i++ }
                (c == '*') || (c == '×') -> { rawTokens.add(Token.Multiply); i++ }
                (c == '/') || (c == '÷') -> { rawTokens.add(Token.Divide); i++ }
                c == '%' -> { rawTokens.add(Token.Modulo); i++ }
                c == '^' -> { rawTokens.add(Token.Power); i++ }
                c == '!' -> { rawTokens.add(Token.Factorial); i++ }
                c == '(' -> { rawTokens.add(Token.LParen); i++ }
                c == ')' -> { rawTokens.add(Token.RParen); i++ }
                c == '√' -> { rawTokens.add(Token.Identifier("sqrt")); i++ }
                c == 'π' -> { rawTokens.add(Token.Identifier("π")); i++ }
                c.isDigit() || c == '.' -> {
                    val start = i
                    var hasDecimal = c == '.'
                    i++
                    while (i < len) {
                        val ch = input[i]
                        if (ch.isDigit()) {
                            i++
                        } else if (ch == '.' && !hasDecimal) {
                            hasDecimal = true
                            i++
                        } else if ((ch == 'e' || ch == 'E') && i + 1 < len) {
                            val next = input[i + 1]
                            if (next.isDigit() || next == '+' || next == '-') {
                                i += 2
                                while (i < len && input[i].isDigit()) {
                                    i++
                                }
                                break
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                    val numStr = input.substring(start, i)
                    val numVal = numStr.toDoubleOrNull() ?: throw EvaluationException.SyntaxError
                    rawTokens.add(Token.Number(numVal))
                }
                c.isLetter() -> {
                    val start = i
                    while (i < len && (input[i].isLetter() || input[i].isDigit() || input[i] == '⁻' || input[i] == '¹')) {
                        i++
                    }
                    val normalized = when (val ident = input.substring(start, i).lowercase()) {
                        "sin", "cos", "tan", "asin", "acos", "atan", "sqrt", "log", "ln", "abs" -> ident
                        "sin⁻¹" -> "asin"
                        "cos⁻¹" -> "acos"
                        "tan⁻¹" -> "atan"
                        "mod" -> "mod"
                        "pi", "π" -> "π"
                        "e" -> "e"
                        else -> ident
                    }
                    if (normalized == "mod") {
                        rawTokens.add(Token.Modulo)
                    } else {
                        rawTokens.add(Token.Identifier(normalized))
                    }
                }
                else -> throw EvaluationException.SyntaxError
            }
        }

        // Insert implicit multiplication tokens where appropriate
        val tokensWithImplicitMul = mutableListOf<Token>()
        for (idx in rawTokens.indices) {
            val curr = rawTokens[idx]
            if (idx > 0) {
                val prev = rawTokens[idx - 1]
                if (shouldInsertImplicitMultiply(prev, curr)) {
                    tokensWithImplicitMul.add(Token.Multiply)
                }
            }
            tokensWithImplicitMul.add(curr)
        }

        return tokensWithImplicitMul
    }

    private fun shouldInsertImplicitMultiply(prev: Token, curr: Token): Boolean {
        val prevIsOperandEnd = when (prev) {
            is Token.Number -> true
            is Token.RParen -> true
            is Token.Factorial -> true
            is Token.Identifier -> isConstant(prev.name)
            else -> false
        }

        val currIsOperandStart = when (curr) {
            is Token.Number -> true
            is Token.LParen -> true
            is Token.Identifier -> isConstant(curr.name) || isFunction(curr.name)
            else -> false
        }

        return prevIsOperandEnd && currIsOperandStart
    }

    private fun isConstant(name: String): Boolean = name == "π" || name == "e"

    private fun isFunction(name: String): Boolean = when (name) {
        "sin", "cos", "tan", "asin", "acos", "atan", "sqrt", "log", "ln", "abs" -> true
        else -> false
    }

    private class Parser(
        private val tokens: List<Token>,
        private val angleUnit: AngleUnit,
    ) {
        private var pos = 0

        fun parse(): Double {
            if (tokens.isEmpty()) return 0.0
            val valResult = parseExpression()
            if (pos < tokens.size) {
                throw EvaluationException.SyntaxError
            }
            return valResult
        }

        private fun peek(): Token? = if (pos < tokens.size) tokens[pos] else null

        private fun consume(): Token {
            val t = peek() ?: throw EvaluationException.SyntaxError
            pos++
            return t
        }

        // expression = term ( ('+' | '-') term )*
        private fun parseExpression(): Double {
            var left = parseTerm()
            while (true) {
                when (peek()) {
                    is Token.Plus -> {
                        consume()
                        val right = parseTerm()
                        left += right
                    }
                    is Token.Minus -> {
                        consume()
                        val right = parseTerm()
                        left -= right
                    }
                    else -> break
                }
            }
            return left
        }

        // term = factor ( ('*' | '/' | '%') factor )*
        private fun parseTerm(): Double {
            var left = parseFactor()
            while (true) {
                when (peek()) {
                    is Token.Multiply -> {
                        consume()
                        val right = parseFactor()
                        left *= right
                    }
                    is Token.Divide -> {
                        consume()
                        val right = parseFactor()
                        if (right == 0.0) {
                            throw EvaluationException.DivisionByZero
                        }
                        left /= right
                    }
                    is Token.Modulo -> {
                        consume()
                        val right = parseFactor()
                        if (right == 0.0) {
                            throw EvaluationException.DivisionByZero
                        }
                        left %= right
                    }
                    else -> break
                }
            }
            return left
        }

        // factor = postfix ( '^' factor )? (Power is right associative)
        private fun parseFactor(): Double {
            val base = parsePostfix()
            if (peek() is Token.Power) {
                consume()
                val exponent = parseFactor()
                val res = base.pow(exponent)
                if (res.isNaN()) throw EvaluationException.DomainError
                return res
            }
            return base
        }

        // postfix = primary ( '!' )*
        private fun parsePostfix(): Double {
            var value = parsePrimary()
            while (true) {
                when (peek()) {
                    is Token.Factorial -> {
                        consume()
                        value = computeFactorial(value)
                    }
                    else -> break
                }
            }
            return value
        }

        // primary = NUMBER | CONSTANT | FUNCTION primary | '-' primary | '+' primary | '(' expression ')'
        private fun parsePrimary(): Double {
            val tok = peek() ?: throw EvaluationException.SyntaxError

            return when (tok) {
                is Token.Plus -> {
                    consume()
                    parsePrimary()
                }
                is Token.Minus -> {
                    consume()
                    -parsePrimary()
                }
                is Token.Number -> {
                    consume()
                    tok.value
                }
                is Token.LParen -> {
                    consume()
                    val exprVal = parseExpression()
                    val closing = peek()
                    if (closing is Token.RParen) {
                        consume()
                    } else {
                        throw EvaluationException.SyntaxError
                    }
                    exprVal
                }
                is Token.Identifier -> {
                    consume()
                    val name = tok.name
                    when {
                        name == "π" -> Math.PI
                        name == "e" -> Math.E
                        isFunction(name) -> {
                            val arg = parsePrimary()
                            evaluateFunction(name, arg, angleUnit)
                        }
                        else -> throw EvaluationException.SyntaxError
                    }
                }
                else -> throw EvaluationException.SyntaxError
            }
        }

        private fun evaluateFunction(name: String, arg: Double, angleUnit: AngleUnit): Double {
            return when (name) {
                "sin" -> {
                    val rad = if (angleUnit == AngleUnit.DEGREE) Math.toRadians(arg) else arg
                    val valSin = sin(rad)
                    if (abs(valSin) < 1e-15) 0.0 else valSin
                }
                "cos" -> {
                    val rad = if (angleUnit == AngleUnit.DEGREE) Math.toRadians(arg) else arg
                    val valCos = cos(rad)
                    if (abs(valCos) < 1e-15) 0.0 else valCos
                }
                "tan" -> {
                    if (angleUnit == AngleUnit.DEGREE) {
                        val norm = abs(arg) % 180
                        if (abs(norm - 90) < 1e-9) {
                            throw EvaluationException.DomainError
                        }
                    }
                    val rad = if (angleUnit == AngleUnit.DEGREE) Math.toRadians(arg) else arg
                    val valTan = tan(rad)
                    if (abs(valTan) < 1e-15) 0.0 else valTan
                }
                "asin" -> {
                    if (arg < -1.0 || arg > 1.0) throw EvaluationException.DomainError
                    val resRad = asin(arg)
                    if (angleUnit == AngleUnit.DEGREE) Math.toDegrees(resRad) else resRad
                }
                "acos" -> {
                    if (arg < -1.0 || arg > 1.0) throw EvaluationException.DomainError
                    val resRad = acos(arg)
                    if (angleUnit == AngleUnit.DEGREE) Math.toDegrees(resRad) else resRad
                }
                "atan" -> {
                    val resRad = atan(arg)
                    if (angleUnit == AngleUnit.DEGREE) Math.toDegrees(resRad) else resRad
                }
                "sqrt" -> {
                    if (arg < 0.0) throw EvaluationException.DomainError
                    sqrt(arg)
                }
                "log" -> {
                    if (arg <= 0.0) throw EvaluationException.DomainError
                    log10(arg)
                }
                "ln" -> {
                    if (arg <= 0.0) throw EvaluationException.DomainError
                    ln(arg)
                }
                "abs" -> abs(arg)
                else -> throw EvaluationException.SyntaxError
            }
        }

        private fun computeFactorial(n: Double): Double {
            if (n < 0 || n != floor(n) || n > 170) {
                throw EvaluationException.DomainError
            }
            var res = 1.0
            val num = n.toInt()
            for (i in 2..num) {
                res *= i
            }
            return res
        }
    }

    private fun cleanupPrecision(value: Double): Double {
        if (abs(value) < 1e-12) return 0.0
        val roundedInt = Math.round(value)
        if (abs(value - roundedInt) < 1e-11) {
            return roundedInt.toDouble()
        }
        return value
    }

    fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Infinity"

        val cleaned = cleanupPrecision(value)
        if (cleaned == 0.0) return "0"

        val bd = BigDecimal(cleaned.toString())
        val absVal = abs(cleaned)

        return if (absVal >= 1e12 || (absVal < 1e-6 && absVal > 0)) {
            String.format("%.8e", cleaned)
                .replace("e+0", "e")
                .replace("e+", "e")
                .replace("e-0", "e-")
        } else {
            bd.setScale(10, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
        }
    }
}
