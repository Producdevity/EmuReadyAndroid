package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameListingDto(
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

@Serializable
data class CreateListingRequest(
    val gameId: String,
    val deviceId: String,
    val performanceRating: Int,
    val playabilityRating: Int,
    val gpuDriver: String,
    val configurationPreset: String,
    val customSettings: String,
    val description: String,
    val screenshotUrls: List<String>,
    val isPublic: Boolean
)

@Serializable
data class UpdateListingRequest(
    val performanceRating: Int,
    val playabilityRating: Int,
    val gpuDriver: String,
    val configurationPreset: String,
    val customSettings: String,
    val description: String,
    val screenshotUrls: List<String>
)