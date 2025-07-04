package com.emuready.emuready.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val developer: String,
    val publisher: String,
    val releaseDate: String,
    val genres: List<String>,
    val averageCompatibility: Float,
    val totalListings: Int,
    val lastUpdated: Long
)

@Serializable
data class GameDetailDto(
    val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val screenshotUrls: List<String>,
    val description: String,
    val releaseDate: String,
    val developer: String,
    val publisher: String,
    val genres: List<String>,
    val compatibilityRatings: Map<String, CompatibilityRatingDto>,
    val listings: List<GameListingDto>,
    val averageRating: Float,
    val totalRatings: Int,
    val lastUpdated: String
)

@Serializable
data class CompatibilityRatingDto(
    val deviceId: String,
    val deviceName: String,
    val performanceRating: Float,
    val playabilityRating: Float,
    val totalReports: Int
)