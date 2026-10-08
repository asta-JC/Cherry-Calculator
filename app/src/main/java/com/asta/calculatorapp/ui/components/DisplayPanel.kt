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
import com.asta.calculatorapp.ui.theme.NeonCherryPrimary
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.SciFiGlassBorder
import com.asta.calculatorapp.ui.theme.SciFiGlassPanel
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextDim
import com.asta.calculatorapp.ui.theme.TextMuted

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

    // Automatically scroll to the right as expression grows
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SciFiGlassPanel)
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NeonCherryPrimary.copy(alpha = 0.5f),
                        SciFiGlassBorder,
                        NeonCherrySecondary.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Status Row: Angle unit indicator, active mode indicators, and history icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Angle Unit Badge + Mode Badges
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

                // Right side: History button
                IconButton(
                    onClick = onToggleHistory,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CherryContainerDeep.copy(alpha = 0.6f))
                        .border(0.8.dp, SciFiGlassBorder, CircleShape)
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

            // Error Banner (Animated slide down)
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

            // Main Expression View (Scrollable horizontally)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(exprScrollState),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expression.isEmpty() && result.isEmpty()) "0" else expression,
                        color = if (expression.isEmpty() && result.isEmpty()) TextDim else TextBright,
                        fontSize = if (expression.length > 18) 24.sp else 32.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Result / Preview Section with glowing effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (result.isNotEmpty()) {
                    // Computed Result
                    Text(
                        text = "= $result",
                        color = NeonCherrySecondary,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                } else if (previewResult.isNotEmpty()) {
                    // Real-time Live Preview (transparent until = is clicked)
                    Text(
                        text = "= $previewResult",
                        color = CherryAccentSoft.copy(alpha = 0.4f),
                        fontSize = 20.sp,
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
                color = if (isActive) activeColor.copy(alpha = 0.6f) else SciFiGlassBorder,
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
