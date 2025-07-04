package com.emuready.emuready.domain.entities

import android.net.Uri

data class GameListing(
    val id: String,
    val gameId: String,
    val userId: String,
    val deviceId: String,
    val performanceRating: Int,
    val playabilityRating: Int,
    val gpuDriver: String,
    val configurationPreset: String,
    val customSettings: String,
    val description: String,
    val screenshotUrls: List<String>,
    val isVerified: Boolean,
    val likes: Int,
    val createdAt: Long,
    val updatedAt: Long
)

data class CreateListingForm(
    val gameId: String = "",
    val deviceId: String = "",
    val performanceRating: Int = 0,
    val playabilityRating: Int = 0,
    val gpuDriver: String = "",
    val configurationPreset: String = "",
    val customSettings: String = "",
    val screenshots: List<Uri> = emptyList(),
    val description: String = "",
    val isPublic: Boolean = true
) {
    fun isValid(): Boolean = listOf(
        gameId.isNotEmpty(),
        deviceId.isNotEmpty(),
        performanceRating in 1..5,
        playabilityRating in 1..5,
        description.length >= 10
    ).all { it }
}