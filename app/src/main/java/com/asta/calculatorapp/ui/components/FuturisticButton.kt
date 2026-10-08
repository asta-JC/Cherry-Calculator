package com.asta.calculatorapp.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asta.calculatorapp.ui.theme.ActionButtonBg
import com.asta.calculatorapp.ui.theme.ActionButtonBgPressed
import com.asta.calculatorapp.ui.theme.CherryAccentSoft
import com.asta.calculatorapp.ui.theme.CherryGlowAura
import com.asta.calculatorapp.ui.theme.DigitButtonBg
import com.asta.calculatorapp.ui.theme.DigitButtonBgPressed
import com.asta.calculatorapp.ui.theme.EqualsGradientEnd
import com.asta.calculatorapp.ui.theme.EqualsGradientStart
import com.asta.calculatorapp.ui.theme.NeonCherryPrimary
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.OperatorButtonBg
import com.asta.calculatorapp.ui.theme.OperatorButtonBgPressed
import com.asta.calculatorapp.ui.theme.SciFiButtonBg
import com.asta.calculatorapp.ui.theme.SciFiButtonBgPressed
import com.asta.calculatorapp.ui.theme.SciFiGlassBorder
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextMuted

enum class FuturisticButtonType {
    DIGIT,
    OPERATOR,
    EQUALS,
    ACTION,
    SCIENTIFIC
}

@Composable
fun FuturisticButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: FuturisticButtonType = FuturisticButtonType.DIGIT,
    isHighlighted: Boolean = false,
    subText: String? = null,
    cornerRadius: Dp = 18.dp,
    onPressChanged: (Boolean) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        onPressChanged(isPressed)
    }

    // Smooth press spring scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.91f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "ButtonScale",
    )

    val shape = RoundedCornerShape(cornerRadius)

    // Color definitions based on button type
    val backgroundColor = when (buttonType) {
        FuturisticButtonType.DIGIT -> if (isPressed) DigitButtonBgPressed else DigitButtonBg
        FuturisticButtonType.OPERATOR -> if (isPressed) OperatorButtonBgPressed else OperatorButtonBg
        FuturisticButtonType.EQUALS -> Color.Transparent // Gradient applied separately
        FuturisticButtonType.ACTION -> if (isPressed) ActionButtonBgPressed else ActionButtonBg
        FuturisticButtonType.SCIENTIFIC -> if (isPressed) SciFiButtonBgPressed else SciFiButtonBg
    }

    val textColor = when (buttonType) {
        FuturisticButtonType.DIGIT -> TextBright
        FuturisticButtonType.OPERATOR -> NeonCherrySecondary
        FuturisticButtonType.EQUALS -> TextBright
        FuturisticButtonType.ACTION -> CherryAccentSoft
        FuturisticButtonType.SCIENTIFIC -> if (isHighlighted) NeonCherrySecondary else TextMuted
    }

    val borderColor = when {
        isPressed -> NeonCherryPrimary
        isHighlighted -> NeonCherrySecondary
        buttonType == FuturisticButtonType.OPERATOR -> CherryGlowAura
        buttonType == FuturisticButtonType.EQUALS -> NeonCherryPrimary
        else -> SciFiGlassBorder
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .zIndex(if ((isPressed || isHighlighted) || (buttonType == FuturisticButtonType.EQUALS)) 1f else 0f)
            .clip(shape)
            .then(
                if (buttonType == FuturisticButtonType.EQUALS) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                EqualsGradientStart,
                                EqualsGradientEnd,
                            ),
                        ),
                    )
                } else {
                    Modifier.background(backgroundColor)
                }
            )
            .border(
                width = if (isPressed || isHighlighted) 1.5.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = if ((isPressed || isHighlighted) || (buttonType == FuturisticButtonType.EQUALS)) {
                        listOf(NeonCherryPrimary, NeonCherrySecondary)
                    } else {
                        listOf(borderColor, borderColor.copy(alpha = 0.3f))
                    }
                ),
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = when (buttonType) {
                    FuturisticButtonType.DIGIT -> 22.sp
                    FuturisticButtonType.OPERATOR -> 22.sp
                    FuturisticButtonType.EQUALS -> 24.sp
                    FuturisticButtonType.ACTION -> 18.sp
                    FuturisticButtonType.SCIENTIFIC -> 15.sp
                },
                fontWeight = if ((buttonType == FuturisticButtonType.EQUALS) || (buttonType == FuturisticButtonType.OPERATOR)) FontWeight.Bold else FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
            if (!subText.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subText,
                    color = NeonCherrySecondary.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
