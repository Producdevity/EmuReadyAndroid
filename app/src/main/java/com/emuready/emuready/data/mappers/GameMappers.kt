package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.local.entities.GameEntity
import com.emuready.emuready.data.remote.dto.GameDto
import com.emuready.emuready.data.remote.dto.GameDetailDto
import com.emuready.emuready.data.remote.dto.CompatibilityRatingDto
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.CompatibilityRating
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun GameDto.toDomain(): Game {
    return Game(
        id = id,
        title = title,
        titleId = titleId,
        coverImageUrl = coverImageUrl,
        developer = developer,
        publisher = publisher,
        releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE),
        genres = genres,
        averageCompatibility = averageCompatibility,
        totalListings = totalListings,
        lastUpdated = lastUpdated,
        isFavorite = false
    )
}

fun GameDetailDto.toDomain(): GameDetail {
    return GameDetail(
        id = id,
        title = title,
        titleId = titleId,
        coverImageUrl = coverImageUrl,
        screenshotUrls = screenshotUrls,
        description = description,
        releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE),
        developer = developer,
        publisher = publisher,
        genres = genres,
        compatibilityRatings = compatibilityRatings.mapValues { it.value.toDomain() },
        listings = listings.map { it.toDomain() },
        averageRating = averageRating,
        totalRatings = totalRatings,
        lastUpdated = Instant.parse(lastUpdated)
    )
}

fun CompatibilityRatingDto.toDomain(): CompatibilityRating {
    return CompatibilityRating(
        deviceId = deviceId,
        deviceName = deviceName,
        performanceRating = performanceRating,
        playabilityRating = playabilityRating,
        totalReports = totalReports
    )
}

fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        title = title,
        titleId = titleId,
        coverImageUrl = coverImageUrl,
        developer = developer,
        publisher = publisher,
        releaseDate = releaseDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        genres = Json.encodeToString(genres),
        averageCompatibility = averageCompatibility,
        totalListings = totalListings,
        lastUpdated = lastUpdated,
        isFavorite = isFavorite
    )
}

fun GameEntity.toDomain(): Game {
    val genresList = try {
        Json.decodeFromString<List<String>>(genres)
    } catch (e: Exception) {
        emptyList()
    }
    
    return Game(
        id = id,
        title = title,
        titleId = titleId,
        coverImageUrl = coverImageUrl,
        developer = developer,
        publisher = publisher,
        releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE),
        genres = genresList,
        averageCompatibility = averageCompatibility,
        totalListings = totalListings,
        lastUpdated = lastUpdated,
        isFavorite = isFavorite
    )
}