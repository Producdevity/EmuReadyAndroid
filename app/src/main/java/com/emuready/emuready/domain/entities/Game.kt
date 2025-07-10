package com.emuready.emuready.domain.entities

/**
 * Game entity representing a Nintendo Switch game in the EmuReady database
 * Supports both Mobile Device listings and PC Configuration listings
 * Per the specification requirements
 */
data class Game(
    val id: String,
    val title: String,
    val coverImageUrl: String?,
    val boxartUrl: String?,
    val bannerUrl: String?,
    val screenshots: List<String> = emptyList(),
    val system: System,
    val averageCompatibility: Float, // 0.0 to 1.0 calculated from performance ratings
    val totalMobileListings: Int,
    val totalPcListings: Int,
    val lastUpdated: Long, // Unix timestamp
    val isFavorite: Boolean = false,
    val releaseDate: String? = null,
    val developer: String? = null,
    val publisher: String? = null,
    val genre: String? = null,
    val description: String? = null
) {
    companion object {
        fun empty() = Game(
            id = "",
            title = "",
            coverImageUrl = null,
            boxartUrl = null,
            bannerUrl = null,
            screenshots = emptyList(),
            system = System.empty(),
            averageCompatibility = 0f,
            totalMobileListings = 0,
            totalPcListings = 0,
            lastUpdated = java.lang.System.currentTimeMillis(),
            isFavorite = false,
            releaseDate = null,
            developer = null,
            publisher = null,
            genre = null,
            description = null
        )
    }
    
    /**
     * Total listings across both mobile devices and PC configurations
     */
    val totalListings: Int get() = totalMobileListings + totalPcListings
    
    /**
     * Compatibility percentage (0-100)
     */
    val compatibilityPercentage: Int get() = (averageCompatibility * 100).toInt()
    
    /**
     * Has any compatibility data
     */
    val hasCompatibilityData: Boolean get() = totalListings > 0
}

