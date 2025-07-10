package com.emuready.emuready.domain.entities

/**
 * Extended game information with additional details for the game detail screen
 * Contains all game information plus related listings, screenshots, and metadata
 */
data class GameDetail(
    val game: Game,
    val description: String?,
    val screenshots: List<String>,
    val developer: String?,
    val publisher: String?,
    val releaseDate: String?,
    val genres: List<String>,
    val recentListings: List<Listing>,
    val topRatedListings: List<Listing>,
    val compatibleDevices: List<Device>,
    val recommendedEmulators: List<Emulator>,
    val averageRating: Float,
    val ratingDistribution: Map<Int, Int>, // star rating to count
    val tags: List<String>,
    val isVerified: Boolean,
    val lastUpdated: Long
) {
    companion object {
        fun fromGame(game: Game) = GameDetail(
            game = game,
            description = null,
            screenshots = emptyList(),
            developer = null,
            publisher = null,
            releaseDate = null,
            genres = emptyList(),
            recentListings = emptyList(),
            topRatedListings = emptyList(),
            compatibleDevices = emptyList(),
            recommendedEmulators = emptyList(),
            averageRating = game.averageCompatibility * 5f, // Convert to 5-star scale
            ratingDistribution = emptyMap(),
            tags = emptyList(),
            isVerified = false,
            lastUpdated = game.lastUpdated
        )
    }
}