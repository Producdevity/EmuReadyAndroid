package com.emuready.emuready.domain.entities

/**
 * Performance levels for game compatibility ratings
 * Based on the EmuReady specification requirements
 */
enum class PerformanceLevel(val value: Int, val displayName: String) {
    UNPLAYABLE(1, "Unplayable"),
    POOR(2, "Poor"),
    PLAYABLE(3, "Playable"),
    GOOD(4, "Good"),
    GREAT(5, "Great"),
    PERFECT(6, "Perfect");

    companion object {
        fun fromValue(value: Int): PerformanceLevel {
            return PerformanceLevel.entries.find { it.value == value } ?: UNPLAYABLE
        }

        fun fromString(value: String): PerformanceLevel {
            return PerformanceLevel.entries.find { it.name.equals(value, ignoreCase = true) } ?: UNPLAYABLE
        }
    }
}
