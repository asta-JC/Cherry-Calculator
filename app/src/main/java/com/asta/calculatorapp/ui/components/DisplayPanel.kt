package com.asta.calculatorapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asta.calculatorapp.ui.theme.CherryAccentSoft
import com.asta.calculatorapp.ui.theme.CherryContainerDeep
import com.asta.calculatorapp.ui.theme.ErrorBright
import com.asta.calculatorapp.ui.theme.ErrorContainer
import com.asta.calculatorapp.ui.theme.GlassPanelBg
import com.asta.calculatorapp.ui.theme.GlassPanelBorder
import com.asta.calculatorapp.ui.theme.NeonCherryPrimary
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextDim
import com.asta.calculatorapp.ui.theme.TextGlowPink
import com.asta.calculatorapp.ui.theme.TextMuted

/**
 * DisplayPanel:
 * Dark glass-style display panel at the top of Cherry Calc.
 *
 * Beginners note:
 * - The display uses a dark frosted glass background with rounded corners (24.dp).
 * - A glowing cherry gradient border wraps the panel.
 * - Upper line shows the ongoing math expression in clean muted text.
 * - Lower line shows the final computed result in large glowing cherry-pink numbers!
 * - Text automatically shrinks if numbers get too long so they never overflow.
 */
@Composable
fun DisplayPanel(
    expression: String,
    previewResult: String,
    result: String,
    isScientific: Boolean,
    isInverse: Boolean,
    hasMemory: Boolean,
    isErrorState: Boolean,
    errorMessage: String?,
    onToggleHistory: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exprScrollState = rememberScrollState()

    // Automatically scroll expression view to the end as numbers are typed
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(GlassPanelBg)
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NeonCherryPrimary.copy(alpha = 0.55f),
                        GlassPanelBorder,
                        NeonCherrySecondary.copy(alpha = 0.35f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- TOP STATUS BAR: Mode badges + History button ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mode indicator pills
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isScientific) {
                        StatusBadge(text = "SCI", isActive = true, activeColor = CherryAccentSoft)
                    }

                    if (isInverse) {
                        StatusBadge(text = "INV", isActive = true, activeColor = NeonCherryPrimary)
                    }

                    if (hasMemory) {
                        StatusBadge(text = "MEM", isActive = true, activeColor = NeonCherrySecondary)
                    }
                }

                // Calculation logs / History icon button
                IconButton(
                    onClick = onToggleHistory,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CherryContainerDeep.copy(alpha = 0.6f))
                        .border(0.8.dp, GlassPanelBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Calculation History",
                        tint = NeonCherrySecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- ANIMATED ERROR BANNER ---
            AnimatedVisibility(
                visible = isErrorState && !errorMessage.isNullOrEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ErrorContainer)
                        .border(1.dp, ErrorBright, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = ErrorBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage ?: "Invalid Expression",
                            color = TextBright,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss Error",
                        tint = TextMuted,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onDismissError)
                    )
                }
            }

            // --- UPPER LINE: Math Expression (Smaller font, scrollable) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(exprScrollState),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val displayText = if (expression.isEmpty() && result.isEmpty()) "0" else expression
                    // Auto-scale font size if expression gets long
                    val exprFontSize = when {
                        displayText.length > 24 -> 18.sp
                        displayText.length > 16 -> 22.sp
                        else -> 28.sp
                    }

                    Text(
                        text = displayText,
                        color = if (expression.isEmpty() && result.isEmpty()) TextDim else TextMuted,
                        fontSize = exprFontSize,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // --- LOWER LINE: Glowing Cherry-Pink Numbers (Result / Preview) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (result.isNotEmpty()) {
                    // Final Calculated Result - Glowing Cherry-Pink
                    val resultFontSize = when {
                        result.length > 18 -> 20.sp
                        result.length > 12 -> 24.sp
                        else -> 32.sp
                    }

                    Text(
                        text = "= $result",
                        color = TextGlowPink,
                        fontSize = resultFontSize,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                } else if (previewResult.isNotEmpty()) {
                    // Live Preview Result
                    val previewFontSize = when {
                        previewResult.length > 18 -> 18.sp
                        else -> 22.sp
                    }

                    Text(
                        text = "= $previewResult",
                        color = CherryAccentSoft.copy(alpha = 0.5f),
                        fontSize = previewFontSize,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) activeColor.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = 0.8.dp,
                color = if (isActive) activeColor.copy(alpha = 0.6f) else GlassPanelBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isActive) activeColor else TextMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
