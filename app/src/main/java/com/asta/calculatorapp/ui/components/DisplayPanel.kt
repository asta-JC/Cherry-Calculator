package com.asta.calculatorapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DisplayPanel(expression: String, previewResult: String, result: String,
    isScientific: Boolean, isInverse: Boolean, hasMemory: Boolean,
    isErrorState: Boolean, errorMessage: String?,
    onDismissError: () -> Unit, modifier: Modifier = Modifier) {
    val confirmed = result.isNotEmpty() && !isErrorState
    val colors = MaterialTheme.colorScheme
    val expressionScroll = rememberScrollState()
    val answerScroll = rememberScrollState()
    val expressionSize by animateFloatAsState(if (confirmed) 23f else 44f, tween(200), label = "Expression size")
    val answerSize by animateFloatAsState(if (confirmed) 54f else 25f, tween(200), label = "Answer size")
    val answerColor by animateColorAsState(if (confirmed) colors.primary else colors.onSurfaceVariant.copy(alpha = 0.6f),
        tween(200), label = "Answer emphasis")
    LaunchedEffect(expression) { expressionScroll.scrollTo(expressionScroll.maxValue) }
    LaunchedEffect(result, previewResult) { answerScroll.scrollTo(answerScroll.maxValue) }
    Surface(modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        color = colors.primaryContainer.copy(alpha = 0.42f),
        border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.12f))) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(when {
                    hasMemory -> "Memory saved"
                    isInverse -> "Inverse functions"
                    confirmed -> "Result"
                    else -> "Your calculation"
                }, Modifier.weight(1f), style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth().heightIn(min = 62.dp),
                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Text(expression.ifEmpty { "0" }, Modifier.horizontalScroll(expressionScroll),
                    fontSize = expressionSize.sp,
                    fontWeight = if (confirmed) FontWeight.Normal else FontWeight.Medium,
                    color = if (confirmed) colors.onSurfaceVariant else colors.onSurface, maxLines = 1)
            }
            Row(Modifier.fillMaxWidth().heightIn(min = 64.dp),
                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                val answer = if (confirmed) result else previewResult
                Text(if (answer.isNotEmpty()) { if (confirmed) answer else "= $answer" }
                    else if (expression.isEmpty()) "Enter a calculation" else "",
                    Modifier.horizontalScroll(answerScroll).semantics {
                        contentDescription = if (confirmed) "Result $result" else if (previewResult.isNotEmpty())
                            "Preview $previewResult" else "No preview"
                    },
                    fontSize = if (answer.isEmpty()) 16.sp else answerSize.sp,
                    fontWeight = if (confirmed) FontWeight.SemiBold else FontWeight.Normal,
                    color = answerColor, maxLines = 1)
            }
            if (isErrorState && errorMessage != null) {
                Surface(color = colors.errorContainer, shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(errorMessage, Modifier.weight(1f), color = colors.onErrorContainer)
                        IconButton(onClick = onDismissError) { Icon(Icons.Default.Close, "Dismiss error") }
                    }
                }
            }
        }
    }
}
