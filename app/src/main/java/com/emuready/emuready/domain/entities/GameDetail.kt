package com.emuready.emuready.domain.entities

import java.time.Instant
import java.time.LocalDate

data class GameDetail(
    val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val screenshotUrls: List<String>,
    val description: String,
    val releaseDate: LocalDate,
    val developer: String,
    val publisher: String,
    val genres: List<String>,
    val compatibilityRatings: Map<String, CompatibilityRating>,
    val listings: List<GameListing>,
    val averageRating: Float,
    val totalRatings: Int,
    val lastUpdated: Instant
)

data class CompatibilityRating(
    val deviceId: String,
    val deviceName: String,
    val performanceRating: Float,
    val playabilityRating: Float,
    val totalReports: Int
)