package com.emuready.emuready.domain.entities

/**
 * Performance scale for compatibility ratings
 */
data class PerformanceScale(
    val id: Int,
    val label: String,
    val description: String
) {
    companion object {
        fun fromId(id: Int): PerformanceScale {
            return when (id) {
                1 -> PerformanceScale(1, "Unplayable", "Game does not run or is unplayable")
                2 -> PerformanceScale(2, "Poor", "Game runs but with major issues")
                3 -> PerformanceScale(3, "Playable", "Game is playable with minor issues")
                4 -> PerformanceScale(4, "Good", "Game runs well with few issues")
                5 -> PerformanceScale(5, "Great", "Game runs great with minor issues")
                6 -> PerformanceScale(6, "Perfect", "Game runs perfectly")
                else -> PerformanceScale(1, "Unknown", "Unknown performance level")
            }
        }
    }
}