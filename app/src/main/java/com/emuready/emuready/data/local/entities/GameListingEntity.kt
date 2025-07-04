package com.emuready.emuready.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_listings")
data class GameListingEntity(
    @PrimaryKey val id: String,
    val gameId: String,
    val userId: String,
    val deviceId: String,
    val performanceRating: Int,
    val playabilityRating: Int,
    val gpuDriver: String,
    val configurationPreset: String,
    val customSettings: String,
    val description: String,
    val screenshotUrls: String, // JSON string
    val isVerified: Boolean,
    val likes: Int,
    val createdAt: Long,
    val updatedAt: Long
)