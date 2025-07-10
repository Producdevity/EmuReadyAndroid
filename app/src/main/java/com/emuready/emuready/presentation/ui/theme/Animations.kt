package com.emuready.emuready.presentation.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.delay

// Award-winning animation system with sophisticated timing
object EmuAnimations {
    
    // Sophisticated easing curves for premium feel
    val PremiumEaseInOut = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val PremiumEaseOut = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val PremiumEaseIn = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
    val BounceEasing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
    val SmoothEasing = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
    
    // Timing constants for consistent animations
    object Duration {
        const val QUICK = 150
        const val NORMAL = 300
        const val SLOW = 500
        const val EXTRA_SLOW = 800
        const val HERO = 1200
        const val STAGGER_DELAY = 50L
    }
    
    // Spring configurations for different UI elements
    val ButtonSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )
    
    val CardSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val HeroSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    // Tween configurations for smooth transitions
    val QuickTween = tween<Float>(
        durationMillis = Duration.QUICK,
        easing = PremiumEaseOut
    )
    
    val NormalTween = tween<Float>(
        durationMillis = Duration.NORMAL,
        easing = PremiumEaseInOut
    )
    
    val SlowTween = tween<Float>(
        durationMillis = Duration.SLOW,
        easing = SmoothEasing
    )
    
    // Keyframe animations for complex movements
    val PulseKeyframes = keyframes<Float> {
        durationMillis = Duration.SLOW
        1.0f at 0 with PremiumEaseInOut
        1.05f at Duration.NORMAL / 2 with PremiumEaseInOut
        1.0f at Duration.NORMAL with PremiumEaseInOut
    }
    
    val WaveKeyframes = keyframes<Float> {
        durationMillis = Duration.HERO
        0f at 0 with PremiumEaseInOut
        1f at Duration.NORMAL with PremiumEaseInOut
        0f at Duration.SLOW with PremiumEaseInOut
        1f at Duration.HERO with PremiumEaseInOut
    }
}

// Enhanced press animation with haptic feedback
@Composable
fun Modifier.premiumPressAnimation(
    scaleDown: Float = 0.96f,
    scaleUp: Float = 1.02f,
    enableHaptics: Boolean = true
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = EmuAnimations.ButtonSpring,
        label = "press_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = EmuAnimations.QuickTween,
        label = "press_elevation"
    )
    
    this
        .scale(scale)
        .graphicsLayer {
            shadowElevation = elevation
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    if (enableHaptics) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    tryAwaitRelease()
                    isPressed = false
                }
            )
        }
}

// Sophisticated hover animation for premium feel
@Composable
fun Modifier.premiumHoverAnimation(
    hoverScale: Float = 1.02f,
    hoverElevation: Float = 12f
): Modifier = composed {
    var isHovered by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isHovered) hoverScale else 1f,
        animationSpec = EmuAnimations.CardSpring,
        label = "hover_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isHovered) hoverElevation else 4f,
        animationSpec = EmuAnimations.NormalTween,
        label = "hover_elevation"
    )
    
    this
        .scale(scale)
        .graphicsLayer {
            shadowElevation = elevation
        }
}

// Shimmer loading animation for premium loading states
@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    var startAnimation by remember { mutableStateOf(false) }
    
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )
    
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    
    LaunchedEffect(key1 = startAnimation) {
        startAnimation = true
    }
    
    this.graphicsLayer {
        alpha = if (startAnimation) 1f else 0f
    }
}

// Stagger animation for list items
@Composable
fun rememberStaggeredListAnimation(
    itemCount: Int,
    staggerDelay: Long = EmuAnimations.Duration.STAGGER_DELAY
): List<State<Float>> {
    return (0 until itemCount).map { index ->
        val animatedValue = remember { Animatable(0f) }
        
        LaunchedEffect(Unit) {
            delay(index * staggerDelay)
            animatedValue.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = EmuAnimations.Duration.NORMAL,
                    easing = EmuAnimations.PremiumEaseOut
                )
            )
        }
        
        animatedValue.asState()
    }
}

// Bounce animation for success states
@Composable
fun Modifier.bounceAnimation(trigger: Boolean): Modifier = composed {
    val scale by animateFloatAsState(
        targetValue = if (trigger) 1.1f else 1f,
        animationSpec = tween(
            durationMillis = EmuAnimations.Duration.QUICK,
            easing = EmuAnimations.BounceEasing
        ),
        label = "bounce_scale"
    )
    
    this.scale(scale)
}

// Pulse animation for attention-grabbing elements
@Composable
fun Modifier.pulseAnimation(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EmuAnimations.SmoothEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    this.scale(scale)
}

// Slide in animation with sophisticated timing
@Composable
fun slideInFromBottom(
    delayMillis: Int = 0,
    durationMillis: Int = EmuAnimations.Duration.NORMAL
): androidx.compose.animation.EnterTransition {
    return androidx.compose.animation.slideInVertically(
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = EmuAnimations.PremiumEaseOut
        ),
        initialOffsetY = { it }
    ) + androidx.compose.animation.fadeIn(
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = EmuAnimations.PremiumEaseOut
        )
    )
}

// Enhanced fade animation
@Composable
fun fadeInWithScale(
    delayMillis: Int = 0,
    durationMillis: Int = EmuAnimations.Duration.NORMAL
): androidx.compose.animation.EnterTransition {
    return androidx.compose.animation.fadeIn(
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = EmuAnimations.PremiumEaseOut
        )
    ) + androidx.compose.animation.scaleIn(
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = EmuAnimations.PremiumEaseOut
        ),
        initialScale = 0.8f
    )
}