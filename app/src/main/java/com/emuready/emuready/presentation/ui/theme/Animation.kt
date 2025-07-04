package com.emuready.emuready.presentation.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.dp

// Animation Durations (inspired by award-winning apps like Stripe, Notion, Linear)
object AnimationDurations {
    const val INSTANT = 0
    const val FAST = 150
    const val NORMAL = 300
    const val SLOW = 500
    const val EXTRA_SLOW = 700
    const val HERO = 1000
}

// Premium Easing Curves
object EasingCurves {
    // Standard Material Design 3.0 curves
    val Standard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val StandardDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
    val StandardAccelerate = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
    
    // Custom premium curves
    val Smooth = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
    val Bouncy = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)
    val Sharp = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
    val Elastic = CubicBezierEasing(0.175f, 0.885f, 0.32f, 1.275f)
    
    // iOS-inspired curves
    val iOSEaseOut = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
    val iOSEaseIn = CubicBezierEasing(0.7f, 0f, 0.84f, 0f)
    val iOSEaseInOut = CubicBezierEasing(0.86f, 0f, 0.07f, 1f)
}

// Spring Animations for natural feel
object SpringSpecs {
    val Gentle = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val Bouncy = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val Quick = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
    )
    
    val Snappy = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )
}

// Transition Specs for different UI elements
object TransitionSpecs {
    // Tween animations with premium easing
    val FastSmooth = tween<Float>(
        durationMillis = AnimationDurations.FAST,
        easing = EasingCurves.Smooth
    )
    
    val NormalSmooth = tween<Float>(
        durationMillis = AnimationDurations.NORMAL,
        easing = EasingCurves.Smooth
    )
    
    val SlowElastic = tween<Float>(
        durationMillis = AnimationDurations.SLOW,
        easing = EasingCurves.Elastic
    )
    
    // Card animations
    val CardEntry = tween<Float>(
        durationMillis = AnimationDurations.NORMAL,
        easing = EasingCurves.iOSEaseOut
    )
    
    val CardExit = tween<Float>(
        durationMillis = AnimationDurations.FAST,
        easing = EasingCurves.iOSEaseIn
    )
    
    // Button press feedback
    val ButtonPress = tween<Float>(
        durationMillis = AnimationDurations.FAST,
        easing = EasingCurves.Sharp
    )
    
    // Hero animations
    val HeroTransition = tween<Float>(
        durationMillis = AnimationDurations.HERO,
        easing = EasingCurves.iOSEaseInOut
    )
}

// Pre-built Enter/Exit Transitions
object EnterExitTransitions {
    val FadeIn = fadeIn(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth))
    val FadeOut = fadeOut(animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth))
    
    val SlideInFromBottom = slideInVertically(
        animationSpec = tween(AnimationDurations.NORMAL, easing = EasingCurves.Smooth),
        initialOffsetY = { it }
    )
    
    val SlideOutToBottom = slideOutVertically(
        animationSpec = tween(AnimationDurations.FAST, easing = EasingCurves.Smooth),
        targetOffsetY = { it }
    )
    
    val FadeSlideIn = FadeIn + SlideInFromBottom
    val FadeSlideOut = FadeOut + SlideOutToBottom
}

// Micro-interaction animations
object MicroAnimations {
    // Scale for button press feedback
    val ButtonPressScale = 0.96f
    
    // Hover scale for cards
    val CardHoverScale = 1.02f
    
    // Ripple scale
    val RippleScale = 2.0f
    
    // Pulse scale for loading states
    val PulseScale = 1.1f
    
    // Rotation for refresh icons
    val RefreshRotation = 360f
}

// Elevation animations
object ElevationAnimations {
    val CardIdle = 2.dp
    val CardHovered = 8.dp
    val CardPressed = 1.dp
    
    val ButtonIdle = 2.dp
    val ButtonHovered = 4.dp
    val ButtonPressed = 0.dp
}

// Stagger animations for lists
object StaggerAnimations {
    const val ItemDelay = 50L
    const val MaxItems = 10
    
    fun calculateDelay(index: Int): Long {
        return (index.coerceAtMost(MaxItems) * ItemDelay)
    }
}