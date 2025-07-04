package com.emuready.emuready.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Premium Brand Colors inspired by award-winning gaming apps
val PrimaryPurple = Color(0xFF6366F1) // Modern indigo
val PrimaryPurpleDark = Color(0xFF4F46E5)
val PrimaryPurpleLight = Color(0xFF8B87FA)

val SecondaryTeal = Color(0xFF06B6D4) // Vibrant cyan
val SecondaryTealDark = Color(0xFF0891B2)
val SecondaryTealLight = Color(0xFF22D3EE)

val AccentOrange = Color(0xFFF59E0B) // Warm amber
val AccentPink = Color(0xFFEC4899) // Vibrant pink
val AccentGreen = Color(0xFF10B981) // Success green
val AccentRed = Color(0xFFEF4444) // Error red

// Neutral colors with depth
val NeutralGray50 = Color(0xFFF9FAFB)
val NeutralGray100 = Color(0xFFF3F4F6)
val NeutralGray200 = Color(0xFFE5E7EB)
val NeutralGray300 = Color(0xFFD1D5DB)
val NeutralGray400 = Color(0xFF9CA3AF)
val NeutralGray500 = Color(0xFF6B7280)
val NeutralGray600 = Color(0xFF4B5563)
val NeutralGray700 = Color(0xFF374151)
val NeutralGray800 = Color(0xFF1F2937)
val NeutralGray900 = Color(0xFF111827)

// Light Theme Colors
val Primary = PrimaryPurple
val OnPrimary = Color.White
val PrimaryContainer = PrimaryPurpleLight
val OnPrimaryContainer = PrimaryPurpleDark

val Secondary = SecondaryTeal
val OnSecondary = Color.White
val SecondaryContainer = Color(0xFFECFDF5)
val OnSecondaryContainer = SecondaryTealDark

val Tertiary = AccentOrange
val OnTertiary = Color.White
val TertiaryContainer = Color(0xFFFEF3C7)
val OnTertiaryContainer = Color(0xFFD97706)

val Error = AccentRed
val OnError = Color.White
val ErrorContainer = Color(0xFFFEE2E2)
val OnErrorContainer = Color(0xFFDC2626)

val Background = Color(0xFFFFFBFF)
val OnBackground = NeutralGray900
val Surface = Color(0xFFFFFBFF)
val OnSurface = NeutralGray900
val SurfaceVariant = NeutralGray100
val OnSurfaceVariant = NeutralGray600

val Outline = NeutralGray300
val OutlineVariant = NeutralGray200
val Scrim = Color(0xFF000000)

// Dark Theme Colors
val PrimaryDark = PrimaryPurpleLight
val OnPrimaryDark = NeutralGray900
val PrimaryContainerDark = PrimaryPurpleDark
val OnPrimaryContainerDark = PrimaryPurpleLight

val SecondaryDark = SecondaryTealLight
val OnSecondaryDark = NeutralGray900
val SecondaryContainerDark = SecondaryTealDark
val OnSecondaryContainerDark = SecondaryTealLight

val TertiaryDark = Color(0xFFFBBF24)
val OnTertiaryDark = NeutralGray900
val TertiaryContainerDark = Color(0xFFD97706)
val OnTertiaryContainerDark = Color(0xFFFEF3C7)

val ErrorDark = Color(0xFFF87171)
val OnErrorDark = NeutralGray900
val ErrorContainerDark = Color(0xFFDC2626)
val OnErrorContainerDark = Color(0xFFFEE2E2)

val BackgroundDark = Color(0xFF0F0F23)
val OnBackgroundDark = NeutralGray100
val SurfaceDark = Color(0xFF1A1B3A)
val OnSurfaceDark = NeutralGray100
val SurfaceVariantDark = NeutralGray800
val OnSurfaceVariantDark = NeutralGray300

val OutlineDark = NeutralGray600
val OutlineVariantDark = NeutralGray700

// Custom Surface Colors for Cards and Elevation
val SurfaceElevated = Color(0xFFFFFFFF)
val SurfaceElevatedDark = Color(0xFF252547)

// Gaming-specific colors
val SuccessGreen = AccentGreen
val WarningAmber = AccentOrange
val InfoBlue = SecondaryTeal
val HighlightPink = AccentPink

// Glassmorphism effects
val GlassOverlay = Color(0x1AFFFFFF)
val GlassOverlayDark = Color(0x1A000000)