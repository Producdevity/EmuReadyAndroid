package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.local.entities.GameListingEntity
import com.emuready.emuready.data.remote.dto.GameListingDto
import com.emuready.emuready.data.remote.dto.CreateListingRequest
import com.emuready.emuready.domain.entities.GameListing
import com.emuready.emuready.domain.entities.CreateListingForm
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun GameListingDto.toDomain(): GameListing {
    return GameListing(
        id = id,
        gameId = gameId,
        userId = userId,
        deviceId = deviceId,
        performanceRating = performanceRating,
        playabilityRating = playabilityRating,
        gpuDriver = gpuDriver,
        configurationPreset = configurationPreset,
        customSettings = customSettings,
        description = description,
        screenshotUrls = screenshotUrls,
        isVerified = isVerified,
        likes = likes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CreateListingForm.toRequest(): CreateListingRequest {
    return CreateListingRequest(
        gameId = gameId,
        deviceId = deviceId,
        performanceRating = performanceRating,
        playabilityRating = playabilityRating,
        gpuDriver = gpuDriver,
        configurationPreset = configurationPreset,
        customSettings = customSettings,
        description = description,
        screenshotUrls = screenshots.map { it.toString() },
        isPublic = isPublic
    )
}

fun GameListing.toEntity(): GameListingEntity {
    return GameListingEntity(
        id = id,
        gameId = gameId,
        userId = userId,
        deviceId = deviceId,
        performanceRating = performanceRating,
        playabilityRating = playabilityRating,
        gpuDriver = gpuDriver,
        configurationPreset = configurationPreset,
        customSettings = customSettings,
        description = description,
        screenshotUrls = Json.encodeToString(screenshotUrls),
        isVerified = isVerified,
        likes = likes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun GameListingEntity.toDomain(): GameListing {
    val screenshotUrlsList = try {
        Json.decodeFromString<List<String>>(screenshotUrls)
    } catch (e: Exception) {
        emptyList()
    }
    
    return GameListing(
        id = id,
        gameId = gameId,
        userId = userId,
        deviceId = deviceId,
        performanceRating = performanceRating,
        playabilityRating = playabilityRating,
        gpuDriver = gpuDriver,
        configurationPreset = configurationPreset,
        customSettings = customSettings,
        description = description,
        screenshotUrls = screenshotUrlsList,
        isVerified = isVerified,
        likes = likes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}