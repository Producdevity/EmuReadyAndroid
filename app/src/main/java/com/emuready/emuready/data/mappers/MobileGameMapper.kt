package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.remote.dto.MobileGame
import com.emuready.emuready.data.remote.dto.MobileSystem as MobileSystemDto
import com.emuready.emuready.data.remote.dto.MobileListing
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.System
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Mappers for converting Mobile API DTOs to Domain entities
 */

fun MobileGame.toDomain(): Game {
    return Game(
        id = this.id,
        title = this.title,
        titleId = this.id, // Using ID as titleId since titleId is not provided in mobile API
        coverImageUrl = this.imageUrl ?: "",
        developer = "", // Not provided in mobile API, using empty string
        publisher = "", // Not provided in mobile API, using empty string
        releaseDate = LocalDate.now(), // Not provided in mobile API, using current date
        genres = emptyList(), // Not provided in mobile API
        averageCompatibility = 0.0f, // Not provided, will be calculated from listings
        totalListings = this._count.listings,
        lastUpdated = java.lang.System.currentTimeMillis(),
        isFavorite = false, // This would be determined from local database
        system = this.system?.toDomain(),
        listingCount = this._count.listings,
        averageRating = 0.0f // Not provided in mobile API, would be calculated from ratings
    )
}

fun MobileSystemDto.toDomain(): System {
    return System(
        id = this.id,
        name = this.name,
        key = this.key ?: "",
        description = "", // Not provided in mobile API
        logoUrl = "", // Not provided in mobile API
        manufacturer = "", // Not provided in mobile API
        releaseYear = null // Not provided in mobile API
    )
}

/**
 * Calculate average compatibility from listings
 */
fun List<MobileListing>.calculateAverageCompatibility(): Float {
    if (isEmpty()) return 0.0f
    
    val totalRank = sumOf { it.performance.rank }
    val averageRank = totalRank.toFloat() / size
    
    // Convert rank to compatibility percentage (assuming rank 1-5, where 5 is best)
    return (averageRank / 5.0f).coerceIn(0.0f, 1.0f)
}