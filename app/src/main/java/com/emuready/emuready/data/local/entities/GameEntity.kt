package com.emuready.emuready.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String,
    val title: String,
    val coverImageUrl: String?,
    val boxartUrl: String?,
    val bannerUrl: String?,
    val systemId: String,
    val systemName: String,
    val averageCompatibility: Float,
    val totalMobileListings: Int,
    val totalPcListings: Int,
    val lastUpdated: Long,
    val isFavorite: Boolean = false
)