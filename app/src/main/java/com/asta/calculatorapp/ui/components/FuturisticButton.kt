package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class FuturisticButtonType { DIGIT, OPERATOR, EQUALS, ACTION, SCIENTIFIC }

@Composable
fun FuturisticButton(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    buttonType: FuturisticButtonType = FuturisticButtonType.DIGIT,
    isHighlighted: Boolean = false,
    cornerRadius: Dp = 18.dp
) {
    val colors = MaterialTheme.colorScheme
    val strong = buttonType == FuturisticButtonType.EQUALS || isHighlighted
    val accent = buttonType == FuturisticButtonType.OPERATOR || buttonType == FuturisticButtonType.ACTION
    val label = when(text) {
        "%" -> "Percentage"; "AC" -> "Clear all"; "DEL" -> "Delete last character"; "=" -> "Equals"
        "MC" -> "Clear memory"; "MR" -> "Recall memory"; "M+" -> "Add to memory"
        "M-" -> "Subtract from memory"; "2nd" -> "Inverse functions"; else -> text
    }
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = if (buttonType == FuturisticButtonType.SCIENTIFIC) 48.dp else 60.dp)
            .semantics { contentDescription = label; if (isHighlighted) selected = true },
        shape = RoundedCornerShape(cornerRadius),
        border = if (buttonType == FuturisticButtonType.DIGIT)
            BorderStroke(1.dp, colors.primary.copy(alpha = 0.1f)) else null,
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (strong) colors.primary else if (accent) colors.primaryContainer else if (buttonType == FuturisticButtonType.DIGIT) colors.primaryContainer.copy(alpha = 0.24f) else colors.surface,
            contentColor = if (strong) colors.onPrimary else if (accent) colors.onPrimaryContainer else colors.onSurface)
    ) {
        if (text == "DEL") Icon(Icons.AutoMirrored.Outlined.Backspace, null)
        else Text(text, fontSize = if (buttonType == FuturisticButtonType.SCIENTIFIC) 15.sp else 24.sp)
    }
}
