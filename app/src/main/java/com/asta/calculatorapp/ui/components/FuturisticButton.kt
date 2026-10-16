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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
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
import com.asta.calculatorapp.ui.theme.CherryOperatorCenter
import com.asta.calculatorapp.ui.theme.CherryOperatorEdge
import com.asta.calculatorapp.ui.theme.CherryOperatorGlow
import com.asta.calculatorapp.ui.theme.CherrySciFiCenter
import com.asta.calculatorapp.ui.theme.CherrySciFiEdge
import com.asta.calculatorapp.ui.theme.CherrySciFiGlow
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextMuted

/**
 * Button categories for Cherry Calc.
 */
enum class FuturisticButtonType {
    DIGIT,      // Classic Cherry Red (0-9 and .)
    OPERATOR,   // Deep Burgundy / Dark Wine (+ - × ÷)
    EQUALS,     // Luminous glowing red equals button (=)
    ACTION,     // Rich Cherry Red (AC, DEL, %)
    SCIENTIFIC  // Rose-Violet Cherry (sin, cos, log, etc.)
}

/**
 * Curvature styles for realistic cherry stems extending upward.
 */
enum class StemStyle {
    LEFT,
    RIGHT,
    SLIGHT_LEFT,
    SLIGHT_RIGHT,
    NONE
}

/**
 * Generates an organic, heart-shaped realistic cherry silhouette.
 * Features the top stem-well notch, rounded plump lobes, and smooth tapered base.
 */
fun createCherryPath(w: Float, h: Float): Path {
    return Path().apply {
        // Start inside top cleft (stem pit depression)
        moveTo(w * 0.50f, h * 0.16f)
        // Left lobe / shoulder
        cubicTo(
            w * 0.38f, h * 0.04f,
            w * 0.18f, h * 0.03f,
            w * 0.09f, h * 0.20f
        )
        // Left flank / cheek
        cubicTo(
            w * 0.01f, h * 0.36f,
            w * 0.01f, h * 0.62f,
            w * 0.10f, h * 0.78f
        )
        // Bottom left to rounded base
        cubicTo(
            w * 0.19f, h * 0.93f,
            w * 0.35f, h * 0.98f,
            w * 0.50f, h * 0.98f
        )
        // Bottom right from base
        cubicTo(
            w * 0.65f, h * 0.98f,
            w * 0.81f, h * 0.93f,
            w * 0.90f, h * 0.78f
        )
        // Right flank / cheek
        cubicTo(
            w * 0.99f, h * 0.62f,
            w * 0.99f, h * 0.36f,
            w * 0.91f, h * 0.20f
        )
        // Right lobe / shoulder back to cleft
        cubicTo(
            w * 0.82f, h * 0.03f,
            w * 0.62f, h * 0.04f,
            w * 0.50f, h * 0.16f
        )
        close()
    }
}

/**
 * Draws the realistic long curved cherry stem reaching upward above the fruit.
 */
fun DrawScope.drawRealisticStem(
    w: Float,
    h: Float,
    stemStyle: StemStyle,
    hasLeaf: Boolean,
    stemRotation: Float
) {
    if (stemStyle == StemStyle.NONE) return

    val start = Offset(w * 0.50f, h * 0.16f)
    val stemReach = h * 0.82f

    val (c1, c2, end) = when (stemStyle) {
        StemStyle.SLIGHT_LEFT -> Triple(
            Offset(w * 0.45f, -stemReach * 0.18f),
            Offset(w * 0.26f, -stemReach * 0.62f),
            Offset(w * 0.32f, -stemReach)
        )
        StemStyle.SLIGHT_RIGHT -> Triple(
            Offset(w * 0.55f, -stemReach * 0.18f),
            Offset(w * 0.74f, -stemReach * 0.62f),
            Offset(w * 0.68f, -stemReach)
        )
        StemStyle.LEFT -> Triple(
            Offset(w * 0.42f, -stemReach * 0.18f),
            Offset(w * 0.16f, -stemReach * 0.58f),
            Offset(w * 0.22f, -stemReach)
        )
        StemStyle.RIGHT -> Triple(
            Offset(w * 0.58f, -stemReach * 0.18f),
            Offset(w * 0.84f, -stemReach * 0.58f),
            Offset(w * 0.78f, -stemReach)
        )
        StemStyle.NONE -> return
    }

    withTransform({
        rotate(stemRotation, pivot = start)
    }) {
        val stemPath = Path().apply {
            moveTo(start.x, start.y)
            cubicTo(c1.x, c1.y, c2.x, c2.y, end.x, end.y)
        }

        // Stem soft ambient shadow
        drawPath(
            path = stemPath,
            color = Color.Black.copy(alpha = 0.35f),
            style = Stroke(width = 3.6.dp.toPx(), cap = StrokeCap.Round)
        )

        // Multi-tone woody/olive stem gradient
        val stemBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF5A4920), // woody base near fruit
                Color(0xFF6B8022), // olive-green shaft
                Color(0xFF7E9728), // fresh vibrant green
                Color(0xFF8B7137)  // woody terminal joint
            ),
            start = start,
            end = end
        )
        drawPath(
            path = stemPath,
            brush = stemBrush,
            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Sunlight highlight along the stem curve
        val stemHighlight = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0x66E2FF75),
                Color.Transparent
            ),
            start = start,
            end = end
        )
        drawPath(
            path = stemPath,
            brush = stemHighlight,
            style = Stroke(width = 0.8.dp.toPx(), cap = StrokeCap.Round)
        )

        // Top pedicel node / woody knot
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFA68D53), Color(0xFF876B34), Color(0xFF4A3818)),
                center = end,
                radius = 3.8.dp.toPx()
            ),
            topLeft = Offset(end.x - 3.dp.toPx(), end.y - 2.5.dp.toPx()),
            size = Size(6.dp.toPx(), 5.dp.toPx())
        )

        // Leaf sprouting on stem (e.g. for ÷ button)
        if (hasLeaf) {
            val leafOrigin = Offset(
                start.x * 0.25f + c1.x * 0.35f + c2.x * 0.25f + end.x * 0.15f,
                start.y * 0.25f + c1.y * 0.35f + c2.y * 0.25f + end.y * 0.15f
            )
            val leafPath = Path().apply {
                moveTo(leafOrigin.x, leafOrigin.y)
                cubicTo(
                    leafOrigin.x + 8.dp.toPx(), leafOrigin.y - 12.dp.toPx(),
                    leafOrigin.x + 18.dp.toPx(), leafOrigin.y - 8.dp.toPx(),
                    leafOrigin.x + 20.dp.toPx(), leafOrigin.y + 3.dp.toPx()
                )
                cubicTo(
                    leafOrigin.x + 14.dp.toPx(), leafOrigin.y + 8.dp.toPx(),
                    leafOrigin.x + 6.dp.toPx(), leafOrigin.y + 5.dp.toPx(),
                    leafOrigin.x, leafOrigin.y
                )
                close()
            }
            drawPath(
                path = leafPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF5D7D1E), Color(0xFF7A9E28), Color(0xFF8EB72F)),
                    start = leafOrigin,
                    end = Offset(leafOrigin.x + 20.dp.toPx(), leafOrigin.y + 3.dp.toPx())
                )
            )
            val veinPath = Path().apply {
                moveTo(leafOrigin.x, leafOrigin.y)
                quadraticTo(
                    leafOrigin.x + 10.dp.toPx(), leafOrigin.y - 2.dp.toPx(),
                    leafOrigin.x + 18.dp.toPx(), leafOrigin.y + 2.dp.toPx()
                )
            }
            drawPath(
                path = veinPath,
                color = Color(0xFFAFE048).copy(alpha = 0.75f),
                style = Stroke(width = 0.8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

/**
 * Draws the 3D realistic glossy cherry fruit body with authentic spherical depth,
 * specular wet gloss highlights, stem cavity depression, and ambient bounce lighting.
 */
fun DrawScope.drawRealisticCherryBody(
    w: Float,
    h: Float,
    centerColor: Color,
    edgeColor: Color,
    glowColor: Color,
    isGlowing: Boolean,
    currentGlowAlpha: Float,
    isPressed: Boolean
) {
    // 1. Soft glowing aura behind the fruit
    if (isGlowing || isPressed) {
        val glowRadius = w * (if (isGlowing) 0.88f else 0.65f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    glowColor.copy(alpha = currentGlowAlpha),
                    glowColor.copy(alpha = currentGlowAlpha * 0.40f),
                    Color.Transparent
                ),
                center = Offset(w * 0.50f, h * 0.55f),
                radius = glowRadius
            ),
            radius = glowRadius,
            center = Offset(w * 0.50f, h * 0.55f)
        )
    }

    // 2. Realistic Cherry Silhouette
    val cherryPath = createCherryPath(w, h)

    // 3. 3D Volume Spherical Base Gradient
    drawPath(
        path = cherryPath,
        brush = Brush.radialGradient(
            colors = listOf(
                centerColor,
                edgeColor,
                Color(0xFF140005)
            ),
            center = Offset(w * 0.40f, h * 0.42f),
            radius = w * 0.72f
        )
    )

    // 4. Translucent Inner Pulp Glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                (if (isGlowing) Color(0xFFFF4D72) else Color(0xFFD6183E)).copy(alpha = 0.55f),
                Color.Transparent
            ),
            center = Offset(w * 0.42f, h * 0.44f),
            radius = w * 0.46f
        ),
        radius = w * 0.46f,
        center = Offset(w * 0.42f, h * 0.44f)
    )

    // 5. Stem Cavity Well (Dark shadow indentation at the top cleft)
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF0F0004).copy(alpha = 0.95f),
                Color(0xFF1D0108).copy(alpha = 0.60f),
                Color.Transparent
            ),
            center = Offset(w * 0.50f, h * 0.16f),
            radius = w * 0.18f
        ),
        topLeft = Offset(w * 0.35f, h * 0.08f),
        size = Size(w * 0.30f, h * 0.16f)
    )

    // 6. Woody Calyx Attachment Pit (Collar where stem inserts into fruit)
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF5A4423), Color(0xFF332412)),
            center = Offset(w * 0.50f, h * 0.165f),
            radius = w * 0.045f
        ),
        topLeft = Offset(w * 0.465f, h * 0.148f),
        size = Size(w * 0.07f, h * 0.035f)
    )

    // 7. Primary Specular Gloss Highlight (High-shine wet reflection on upper-left shoulder)
    withTransform({
        rotate(degrees = -32f, pivot = Offset(w * 0.28f, h * 0.22f))
    }) {
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.88f),
                    Color(0xFFFFD4DE).copy(alpha = 0.35f),
                    Color.Transparent
                ),
                center = Offset(w * 0.28f, h * 0.22f),
                radius = w * 0.16f
            ),
            topLeft = Offset(w * 0.15f, h * 0.15f),
            size = Size(w * 0.26f, h * 0.14f)
        )
    }

    // Pinpoint brilliant reflection dot
    drawCircle(
        color = Color.White.copy(alpha = 0.92f),
        radius = w * 0.022f,
        center = Offset(w * 0.34f, h * 0.19f)
    )

    // Secondary subtle soft gloss on left flank
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
            center = Offset(w * 0.18f, h * 0.40f),
            radius = w * 0.075f
        ),
        radius = w * 0.075f,
        center = Offset(w * 0.18f, h * 0.40f)
    )

    // 8. Ambient Rim Bounce Light (Reflected warm light on the lower-right shadow edge)
    drawArc(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFFFF6D8D).copy(alpha = 0.28f),
                Color.Transparent
            ),
            center = Offset(w * 0.50f, h * 0.55f)
        ),
        startAngle = 25f,
        sweepAngle = 75f,
        useCenter = false,
        topLeft = Offset(w * 0.06f, h * 0.06f),
        size = Size(w * 0.88f, h * 0.88f),
        style = Stroke(width = 2.0.dp.toPx(), cap = StrokeCap.Round)
    )
}

/**
 * FuturisticButton:
 * Renders a realistic 3D glossy cherry calculator button with long curved stem.
 */
@Composable
fun FuturisticButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: FuturisticButtonType = FuturisticButtonType.DIGIT,
    stemStyle: StemStyle = StemStyle.NONE,
    hasLeaf: Boolean = false,
    isHighlighted: Boolean = false,
    subText: String? = null,
    cornerRadius: Dp = 18.dp, // Maintained for backwards API compatibility
    onPressChanged: (Boolean) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        onPressChanged(isPressed)
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "CherrySquishScale"
    )

    val stemRotation by animateFloatAsState(
        targetValue = if (isPressed) -5f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "StemWiggleRotation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "EqualsGlowPulse")
    val equalsPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EqualsPulseAlpha"
    )

    val isEquals = buttonType == FuturisticButtonType.EQUALS
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

    val currentGlowAlpha = when {
        isPressed -> 0.95f
        isEquals -> equalsPulseAlpha
        isHighlighted -> 0.70f
        else -> 0.40f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.02f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = false
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Custom spring animation handles click feedback
                onClick = onClick
            )
            .drawBehind {
                // Draw realistic stem reaching upward
                drawRealisticStem(
                    w = size.width,
                    h = size.height,
                    stemStyle = stemStyle,
                    hasLeaf = hasLeaf,
                    stemRotation = stemRotation
                )
                // Draw 3D realistic cherry fruit body
                drawRealisticCherryBody(
                    w = size.width,
                    h = size.height,
                    centerColor = centerColor,
                    edgeColor = edgeColor,
                    glowColor = glowColor,
                    isGlowing = isEquals,
                    currentGlowAlpha = currentGlowAlpha,
                    isPressed = isPressed
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = text,
                color = when (buttonType) {
                    FuturisticButtonType.SCIENTIFIC -> if (isHighlighted) TextBright else TextMuted
                    else -> TextBright
                },
                fontSize = when (buttonType) {
                    FuturisticButtonType.DIGIT -> 24.sp
                    FuturisticButtonType.OPERATOR -> 24.sp
                    FuturisticButtonType.EQUALS -> 26.sp
                    FuturisticButtonType.ACTION -> 18.sp
                    FuturisticButtonType.SCIENTIFIC -> 14.sp
                },
                fontWeight = when (buttonType) {
                    FuturisticButtonType.DIGIT -> FontWeight.Medium
                    FuturisticButtonType.OPERATOR -> FontWeight.Normal
                    FuturisticButtonType.EQUALS -> FontWeight.Medium
                    FuturisticButtonType.ACTION -> FontWeight.Normal
                    FuturisticButtonType.SCIENTIFIC -> FontWeight.Medium
                },
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
            if (!subText.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = subText,
                    color = NeonCherrySecondary.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * TwinCherryButton:
 * Renders two connected twin cherries for "(" and ")" in Row 5, sharing an inverted Y-stem with a green leaf.
 */
@Composable
fun TwinCherryButton(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPressChanged: (Boolean) -> Unit = {}
) {
    val leftInteraction = remember { MutableInteractionSource() }
    val rightInteraction = remember { MutableInteractionSource() }
    val isLeftPressed by leftInteraction.collectIsPressedAsState()
    val isRightPressed by rightInteraction.collectIsPressedAsState()

    LaunchedEffect(isLeftPressed, isRightPressed) {
        onPressChanged(isLeftPressed || isRightPressed)
    }

    val leftScale by animateFloatAsState(
        targetValue = if (isLeftPressed) 0.90f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LeftCherrySquish"
    )

    val rightScale by animateFloatAsState(
        targetValue = if (isRightPressed) 0.90f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "RightCherrySquish"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.02f)
            .graphicsLayer { clip = false }
    ) {
        // Shared Y-stems and Leaf drawn on background Canvas
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { clip = false }
        ) {
            val w = size.width
            val h = size.height

            val leftPit = Offset(w * 0.30f, h * 0.70f)
            val rightPit = Offset(w * 0.70f, h * 0.74f)
            val junction = Offset(w * 0.52f, -h * 0.35f)
            val topNode = Offset(w * 0.48f, -h * 0.65f)

            // Left branch stem
            val leftStemPath = Path().apply {
                moveTo(leftPit.x, leftPit.y)
                cubicTo(
                    w * 0.32f, h * 0.20f,
                    w * 0.44f, -h * 0.10f,
                    junction.x, junction.y
                )
            }
            // Right branch stem
            val rightStemPath = Path().apply {
                moveTo(rightPit.x, rightPit.y)
                cubicTo(
                    w * 0.68f, h * 0.22f,
                    w * 0.58f, -h * 0.10f,
                    junction.x, junction.y
                )
            }
            // Top single stem
            val topStemPath = Path().apply {
                moveTo(junction.x, junction.y)
                quadraticTo(w * 0.50f, -h * 0.50f, topNode.x, topNode.y)
            }

            // Stem shadow
            drawPath(leftStemPath, Color.Black.copy(alpha = 0.3f), style = Stroke(3.2.dp.toPx(), cap = StrokeCap.Round))
            drawPath(rightStemPath, Color.Black.copy(alpha = 0.3f), style = Stroke(3.2.dp.toPx(), cap = StrokeCap.Round))
            drawPath(topStemPath, Color.Black.copy(alpha = 0.3f), style = Stroke(3.2.dp.toPx(), cap = StrokeCap.Round))

            // Stems with gradient
            val stemBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF5A4920), Color(0xFF6B8022), Color(0xFF7E9728), Color(0xFF8B7137)),
                start = Offset(w * 0.5f, h),
                end = topNode
            )
            drawPath(leftStemPath, stemBrush, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round))
            drawPath(rightStemPath, stemBrush, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round))
            drawPath(topStemPath, stemBrush, style = Stroke(2.4.dp.toPx(), cap = StrokeCap.Round))

            // Pedicel knot at top
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFA68D53), Color(0xFF876B34), Color(0xFF4A3818)),
                    center = topNode,
                    radius = 3.5.dp.toPx()
                ),
                topLeft = Offset(topNode.x - 2.5.dp.toPx(), topNode.y - 2.dp.toPx()),
                size = Size(5.dp.toPx(), 4.dp.toPx())
            )

            // Leaf sprouting from junction to the left
            val leafPath = Path().apply {
                moveTo(junction.x, junction.y)
                cubicTo(
                    junction.x - 12.dp.toPx(), junction.y - 12.dp.toPx(),
                    junction.x - 22.dp.toPx(), junction.y - 8.dp.toPx(),
                    junction.x - 24.dp.toPx(), junction.y - 2.dp.toPx()
                )
                cubicTo(
                    junction.x - 18.dp.toPx(), junction.y + 4.dp.toPx(),
                    junction.x - 8.dp.toPx(), junction.y + 3.dp.toPx(),
                    junction.x, junction.y
                )
                close()
            }
            drawPath(
                path = leafPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF5D7D1E), Color(0xFF7A9E28), Color(0xFF8EB72F)),
                    start = junction,
                    end = Offset(junction.x - 24.dp.toPx(), junction.y - 2.dp.toPx())
                )
            )
            val veinPath = Path().apply {
                moveTo(junction.x, junction.y)
                quadraticTo(
                    junction.x - 12.dp.toPx(), junction.y - 4.dp.toPx(),
                    junction.x - 22.dp.toPx(), junction.y - 2.dp.toPx()
                )
            }
            drawPath(
                path = veinPath,
                color = Color(0xFFAFE048).copy(alpha = 0.75f),
                style = Stroke(width = 0.8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Left Cherry "("
        Box(
            modifier = Modifier
                .fillMaxWidth(0.56f)
                .aspectRatio(1.02f)
                .align(Alignment.BottomStart)
                .graphicsLayer {
                    scaleX = leftScale
                    scaleY = leftScale
                    clip = false
                }
                .clickable(
                    interactionSource = leftInteraction,
                    indication = null,
                    onClick = onLeftClick
                )
                .drawBehind {
                    drawRealisticCherryBody(
                        w = size.width,
                        h = size.height,
                        centerColor = CherryClassicCenter,
                        edgeColor = CherryClassicEdge,
                        glowColor = CherryClassicGlow,
                        isGlowing = false,
                        currentGlowAlpha = 0.4f,
                        isPressed = isLeftPressed
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.padding(top = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "(",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Right Cherry ")"
        Box(
            modifier = Modifier
                .fillMaxWidth(0.56f)
                .aspectRatio(1.02f)
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    scaleX = rightScale
                    scaleY = rightScale
                    clip = false
                }
                .clickable(
                    interactionSource = rightInteraction,
                    indication = null,
                    onClick = onRightClick
                )
                .drawBehind {
                    drawRealisticCherryBody(
                        w = size.width,
                        h = size.height,
                        centerColor = CherryClassicCenter,
                        edgeColor = CherryClassicEdge,
                        glowColor = CherryClassicGlow,
                        isGlowing = false,
                        currentGlowAlpha = 0.4f,
                        isPressed = isRightPressed
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.padding(top = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ")",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
