package com.asta.calculatorapp.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// CHERRY CALC COLOR PALETTE
// Centralized color tokens for easy customization and theme adjustments.
// ============================================================================

// Dark Background Colors (Near black with a subtle deep cherry tint)
val DarkBgPrimary = Color(0xFF090508)      // Deep dark cherry black for main background
val DarkBgSecondary = Color(0xFF130A11)    // Dark glass panel background

// Glass Panel & Border Styling
val GlassPanelBg = Color(0xFF1C0D18)       // Dark frosted glass panel background
val GlassPanelBorder = Color(0xFF3D162E)   // Subtle glass border color
val GlassGlowColor = Color(0x66FF1744)     // Outer glow tint for panels
val SciFiGlassPanel = GlassPanelBg
val SciFiGlassBorder = GlassPanelBorder

// Stem & Leaf Colors
val CherryStemGreen = Color(0xFF4CAF50)    // Fresh green for cherry stems
val CherryStemDark = Color(0xFF2E7D32)     // Darker shading for stem curvature
val CherryLeafGreen = Color(0xFF81C784)    // Vibrant leaf green

// 1. Classic Cherry Red (For Number Buttons 0-9 and .)
val CherryClassicCenter = Color(0xFFB5092B)  // Vibrant deep cherry red (center highlight)
val CherryClassicEdge = Color(0xFF4A0012)    // Deep dark burgundy red (outer edge)
val CherryClassicGlow = Color(0x66B5092B)    // Ambient glow behind classic cherry

// 2. Deep Burgundy / Dark Cherry (For Operator Buttons +, -, ×, ÷)
val CherryOperatorCenter = Color(0xFFA30626) // Deep crimson center
val CherryOperatorEdge = Color(0xFF3B000E)   // Dark wine edge
val CherryOperatorGlow = Color(0x66A30626)   // Rich burgundy glow

// 3. Bright Red-to-Pink (For Equals Button =)
val CherryEqualsStart = Color(0xFFFF2655)    // Bright glowing cherry red
val CherryEqualsEnd = Color(0xFFC7002E)      // Deep radiant ruby
val CherryEqualsGlow = Color(0xFFFF1744)     // Strong pulsing glow aura

// 4. Lighter Pink-Rose Cherry (For Clear, Delete, %, ± Action Buttons)
val CherryActionCenter = Color(0xFFB0082A)   // Classic rich cherry center
val CherryActionEdge = Color(0xFF450011)     // Deep wine edge
val CherryActionGlow = Color(0x66B0082A)     // Soft cherry glow

// 5. Scientific Buttons Cherry Theme
val CherrySciFiCenter = Color(0xFFC2185B)    // Rose-violet center
val CherrySciFiEdge = Color(0xFF3B0A22)      // Dark rose edge
val CherrySciFiGlow = Color(0x44C2185B)      // Subdued sci-fi glow

// Legacy color token mappings for backwards compatibility across existing components
val NeonCherryPrimary = Color(0xFFFF1744)
val NeonCherrySecondary = Color(0xFFFF2A6D)
val CherryAccentSoft = Color(0xFFFF6289)
val CherryContainerDeep = Color(0xFF3A0818)
val CherryContainerHover = Color(0xFF590C25)
val CherryGlowAura = Color(0x99FF1744)

val DigitButtonBg = CherryClassicEdge
val DigitButtonBgPressed = CherryClassicCenter
val OperatorButtonBg = CherryOperatorEdge
val OperatorButtonBgPressed = CherryOperatorCenter
val ActionButtonBg = CherryActionEdge
val ActionButtonBgPressed = CherryActionCenter
val SciFiButtonBg = CherrySciFiEdge
val SciFiButtonBgPressed = CherrySciFiCenter

val EqualsGradientStart = CherryEqualsStart
val EqualsGradientEnd = CherryEqualsEnd

// Text & Display Highlights
val TextBright = Color(0xFFFFFFFF)           // Pure white for crisp readability
val TextGlowPink = Color(0xFFFF8DA1)         // Glowing cherry-pink text
val TextMuted = Color(0xFFC4A5B7)            // Muted pinkish silver for secondary text
val TextDim = Color(0xFF6B4D5E)              // Dim text for zero/placeholders
val GlossHighlight = Color(0xFFFFFFFF)       // White specular highlight for wet glossy effect

// Error State Colors
val ErrorBright = Color(0xFFFF3366)          // Bright neon error red
val ErrorContainer = Color(0xFF380815)       // Dark error background
