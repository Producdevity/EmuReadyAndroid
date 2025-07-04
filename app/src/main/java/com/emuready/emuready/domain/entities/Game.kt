package com.emuready.emuready.domain.entities

import java.time.LocalDate

data class Game(
    val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val developer: String,
    val publisher: String,
    val releaseDate: LocalDate,
    val genres: List<String>,
    val averageCompatibility: Float,
    val totalListings: Int,
    val lastUpdated: Long,
    val isFavorite: Boolean = false
) {
    companion object {
        fun empty() = Game(
            id = "",
            title = "",
            titleId = "",
            coverImageUrl = "",
            developer = "",
            publisher = "",
            releaseDate = LocalDate.now(),
            genres = emptyList(),
            averageCompatibility = 0f,
            totalListings = 0,
            lastUpdated = System.currentTimeMillis(),
            isFavorite = false
        )
    }
}