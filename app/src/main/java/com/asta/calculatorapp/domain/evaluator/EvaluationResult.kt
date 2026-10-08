package com.asta.calculatorapp.domain.evaluator

sealed class EvaluationResult {
    data class Success(
        val value: Double,
        val formattedResult: String
    ) : EvaluationResult()

    sealed class Error(val message: String) : EvaluationResult() {
        object DivisionByZero : Error("Cannot divide by zero")
        object DomainError : Error("Domain error")
        object SyntaxError : Error("Syntax error")
        data class CustomError(val msg: String) : Error(msg)
    }
}
