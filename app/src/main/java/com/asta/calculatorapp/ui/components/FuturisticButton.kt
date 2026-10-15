package com.asta.calculatorapp.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.asta.calculatorapp.ui.theme.CherryActionCenter
import com.asta.calculatorapp.ui.theme.CherryActionEdge
import com.asta.calculatorapp.ui.theme.CherryActionGlow
import com.asta.calculatorapp.ui.theme.CherryClassicCenter
import com.asta.calculatorapp.ui.theme.CherryClassicEdge
import com.asta.calculatorapp.ui.theme.CherryClassicGlow
import com.asta.calculatorapp.ui.theme.CherryEqualsEnd
import com.asta.calculatorapp.ui.theme.CherryEqualsGlow
import com.asta.calculatorapp.ui.theme.CherryEqualsStart
import com.asta.calculatorapp.ui.theme.CherryLeafGreen
import com.asta.calculatorapp.ui.theme.CherryOperatorCenter
import com.asta.calculatorapp.ui.theme.CherryOperatorEdge
import com.asta.calculatorapp.ui.theme.CherryOperatorGlow
import com.asta.calculatorapp.ui.theme.CherrySciFiCenter
import com.asta.calculatorapp.ui.theme.CherrySciFiEdge
import com.asta.calculatorapp.ui.theme.CherrySciFiGlow
import com.asta.calculatorapp.ui.theme.CherryStemGreen
import com.asta.calculatorapp.ui.theme.GlossHighlight
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextMuted

/**
 * Button categories for Cherry Calc.
 * Determines the cherry shade, radial gradient, and glow intensity.
 */
enum class FuturisticButtonType {
    DIGIT,      // Classic Cherry Red (0-9 and .)
    OPERATOR,   // Deep Burgundy / Dark Cherry (+ - × ÷)
    EQUALS,     // Bright Red-to-Pink pulsing Equals Cherry (=)
    ACTION,     // Light Pink-Rose Cherry (AC, DEL, %, ±)
    SCIENTIFIC  // Rose-Violet Cherry (sin, cos, log, etc.)
}

/**
 * FuturisticButton / CherryButton:
 * A 3D glossy cherry-shaped calculator button.
 *
 * Beginner Explanation of Cherry UI Elements:
 * 1. STEM & LEAF: Drawn at the top using Compose Canvas paths. Wiggles when pressed!
 * 2. CHERRY BODY: A round circle with a 3D radial gradient (lighter in center, deep red at edges).
 * 3. GLOSS HIGHLIGHT: Top-left white/pink specular shine making the cherry look wet and shiny.
 * 4. PULSING GLOW: Soft glowing aura behind the button. Equals (=) pulses continuously!
 * 5. PRESS ANIMATION: Squishes down slightly (scale 92%) with a spring bounce when tapped.
 */
@Composable
fun FuturisticButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: FuturisticButtonType = FuturisticButtonType.DIGIT,
    isHighlighted: Boolean = false,
    subText: String? = null,
    cornerRadius: Dp = 18.dp, // Maintained for API compatibility
    onPressChanged: (Boolean) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        onPressChanged(isPressed)
    }

    // 1. Squish press scale animation: squishes down to ~92% when tapped
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "CherrySquishScale"
    )

    // 2. Stem wiggle animation: stem sways -8 degrees on tap
    val stemRotation by animateFloatAsState(
        targetValue = if (isPressed) -8f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "StemWiggleRotation"
    )

    // 3. Equals button continuous soft pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "EqualsGlowPulse")
    val equalsPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EqualsPulseAlpha"
    )

    // 4. Color gradients defined for each button type
    val (centerColor, edgeColor, glowColor) = when (buttonType) {
        FuturisticButtonType.DIGIT -> Triple(CherryClassicCenter, CherryClassicEdge, CherryClassicGlow)
        FuturisticButtonType.OPERATOR -> Triple(CherryOperatorCenter, CherryOperatorEdge, CherryOperatorGlow)
        FuturisticButtonType.EQUALS -> Triple(CherryEqualsStart, CherryEqualsEnd, CherryEqualsGlow)
        FuturisticButtonType.ACTION -> Triple(CherryActionCenter, CherryActionEdge, CherryActionGlow)
        FuturisticButtonType.SCIENTIFIC -> if (isHighlighted) {
            Triple(CherryOperatorCenter, CherryOperatorEdge, CherryOperatorGlow)
        } else {
            Triple(CherrySciFiCenter, CherrySciFiEdge, CherrySciFiGlow)
        }
    }

    // Glow aura opacity calculation
    val currentGlowAlpha = when {
        isPressed -> 0.95f
        buttonType == FuturisticButtonType.EQUALS -> equalsPulseAlpha
        isHighlighted -> 0.70f
        else -> 0.40f
    }

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .zIndex(if (isPressed || isHighlighted || buttonType == FuturisticButtonType.EQUALS) 10f else 0f)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Custom spring animation handles click feedback
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CHERRY STEM & LEAF ---
        // Curved green stem coming out of top of cherry body, with a tiny leaf
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(14.dp)
                .graphicsLayer {
                    rotationZ = stemRotation
                    transformOrigin = TransformOrigin(0.5f, 1.0f) // Pivot at bottom where stem joins cherry
                }
        ) {
            val width = size.width
            val height = size.height

            // Stem path curve
            val stemPath = Path().apply {
                moveTo(width * 0.5f, height) // Base of stem
                cubicTo(
                    width * 0.48f, height * 0.65f,
                    width * 0.28f, height * 0.35f,
                    width * 0.35f, 0f            // Top of stem
                )
            }

            drawPath(
                path = stemPath,
                color = CherryStemGreen,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Tiny leaf on the stem
            val leafPath = Path().apply {
                moveTo(width * 0.38f, height * 0.40f)
                quadraticTo(
                    width * 0.72f, height * 0.15f,
                    width * 0.82f, height * 0.30f
                )
                quadraticTo(
                    width * 0.55f, height * 0.55f,
                    width * 0.38f, height * 0.40f
                )
            }

            drawPath(
                path = leafPath,
                color = CherryLeafGreen
            )
        }

        // --- ROUND GLOSSY CHERRY BODY ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Perfect round circle
                .drawBehind {
                    // Soft glowing aura behind the cherry
                    val glowRadius = size.width * 0.62f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = currentGlowAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = glowRadius
                        ),
                        radius = glowRadius,
                        center = center
                    )
                }
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(centerColor, edgeColor),
                        // Top-left offset center gives a 3D spherical lighting effect
                        center = Offset(0.35f, 0.30f),
                        radius = Float.POSITIVE_INFINITY
                    )
                )
                .drawWithContent {
                    drawContent() // Draw the text label

                    // Gloss Specular Highlight (top-left white/pink shine)
                    val highlightRadius = size.width * 0.24f
                    val highlightCenter = Offset(size.width * 0.30f, size.height * 0.25f)

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GlossHighlight.copy(alpha = 0.65f),
                                GlossHighlight.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            center = highlightCenter,
                            radius = highlightRadius
                        ),
                        radius = highlightRadius,
                        center = highlightCenter
                    )

                    // Top-left curved rim reflection arc
                    drawArc(
                        color = Color.White.copy(alpha = 0.30f),
                        startAngle = 180f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(size.width * 0.10f, size.height * 0.10f),
                        size = Size(size.width * 0.80f, size.height * 0.80f),
                        style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // --- BUTTON LABEL ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    color = when (buttonType) {
                        FuturisticButtonType.DIGIT -> TextBright
                        FuturisticButtonType.OPERATOR -> TextBright
                        FuturisticButtonType.EQUALS -> TextBright
                        FuturisticButtonType.ACTION -> TextBright
                        FuturisticButtonType.SCIENTIFIC -> if (isHighlighted) TextBright else TextMuted
                    },
                    fontSize = when (buttonType) {
                        FuturisticButtonType.DIGIT -> 22.sp
                        FuturisticButtonType.OPERATOR -> 22.sp
                        FuturisticButtonType.EQUALS -> 26.sp
                        FuturisticButtonType.ACTION -> 18.sp
                        FuturisticButtonType.SCIENTIFIC -> 14.sp
                    },
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                if (!subText.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subText,
                        color = NeonCherrySecondary.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
