package com.emuready.emuready.core.constants

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing

object AnimationConstants {
    const val FAST_ANIMATION = 200
    const val NORMAL_ANIMATION = 300
    const val SLOW_ANIMATION = 500
    
    val FastEasing = FastOutSlowInEasing
    val StandardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
}