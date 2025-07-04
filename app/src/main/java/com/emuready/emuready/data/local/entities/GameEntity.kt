package com.emuready.emuready.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val developer: String,
    val publisher: String,
    val releaseDate: String,
    val genres: String, // JSON string
    val averageCompatibility: Float,
    val totalListings: Int,
    val lastUpdated: Long,
    val isFavorite: Boolean = false
)